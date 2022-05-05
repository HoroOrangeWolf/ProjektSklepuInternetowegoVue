package com.computer.parts.shop.Product;

import com.azure.core.annotation.Get;
import com.computer.parts.shop.Attachment.Attachment;
import com.computer.parts.shop.BlobService.BlobService;
import com.computer.parts.shop.Category.Category;
import com.computer.parts.shop.Category.CategoryService;
import com.computer.parts.shop.Opinion.OpinionService;
import com.computer.parts.shop.Pageable.Pageable;
import com.computer.parts.shop.Product.Request.ProductRequest;
import com.computer.parts.shop.Product.Response.ProductDTO;
import com.computer.parts.shop.Specification.Specification;
import com.computer.parts.shop.Specification.SpecificationService;
import com.computer.parts.shop.Views.ProductView;
import com.fasterxml.jackson.annotation.JsonView;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/product")
@AllArgsConstructor
@Log4j2
public class ProductController {

  private ProductService productService;
  private CategoryService categoryService;
  private SpecificationService specificationService;

  private OpinionService opinionService;

  private BlobService blobService;

  @PostMapping(value = "/admin")
  public void addProduct(@Valid @RequestBody ProductRequest productRequest) {
    Category category = null;

    if (productRequest.getCategoryId() != null) category =
      categoryService.getCategoryById(productRequest.getCategoryId());

    Product product = new Product(
      productRequest.getName(),
      productRequest.getProducer(),
      productRequest.getPrice(),
      productRequest.getDescription(),
      category
    );

    List<Specification> specificationList = productRequest
      .getSpecifications()
      .stream()
      .map(f -> new Specification(f.getKeyName(), f.getKeyValue(), product))
      .toList();

    List<String> attachmentList = productRequest
      .getAttachments()
      .stream()
      .map(Attachment::getBlob)
      .toList();

    String uploadBlob = blobService.uploadBlob(attachmentList);

    product.setFileName(uploadBlob);

    productService.addProduct(product);

    specificationService.saveAllSpecification(specificationList);
  }

  @GetMapping("/fast")
  public Pageable<Map<String, Object>> getSimpleProducts(
    @RequestParam(
      value = "searchBy",
      defaultValue = "",
      required = false
    ) String searchBy
  ) {
    List<Product> products = productService.getProducts(
      0,
      20,
      "name",
      Sort.Direction.ASC,
      searchBy,
      BigDecimal.ZERO,
      BigDecimal.valueOf(Double.MAX_VALUE)
    );

    List<Map<String, Object>> maps = products
      .stream()
      .map(f -> {
        Map<String, Object> mapped = new TreeMap<>();
        mapped.put("productId", f.getId());
        mapped.put("name", f.getName());

        return mapped;
      })
      .toList();

    return new Pageable<>((long) products.size(), maps);
  }

  @GetMapping
  public Pageable<ProductDTO> getProductByRequest(
    @RequestParam(
      value = "categoryId",
      required = false,
      defaultValue = "-1"
    ) Long categoryId,
    @RequestParam(
      value = "page",
      required = false,
      defaultValue = "0"
    ) Integer page,
    @RequestParam(
      value = "limit",
      required = false,
      defaultValue = "20"
    ) Integer limit,
    @RequestParam(
      value = "sortBy",
      required = false,
      defaultValue = "id"
    ) String sortBy,
    @RequestParam(
      value = "searchBy",
      required = false,
      defaultValue = ""
    ) String searchBy,
    @RequestParam(
      value = "sort",
      required = false,
      defaultValue = "ASC"
    ) Sort.Direction direction,
    @RequestParam(
      value = "priceFrom",
      defaultValue = "0",
      required = false
    ) BigDecimal priceStartAt,
    @RequestParam(
      value = "priceTo",
      defaultValue = Double.MAX_VALUE + "",
      required = false
    ) BigDecimal priceEndAt
  ) throws ExecutionException, InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(6);
    ExecutorService countService = Executors.newSingleThreadExecutor();

    if (categoryId < 0) {
      Future<Long> countAllProducts = countService.submit(() ->
        productService.countAllProductsAlikeToSearchBy(
          searchBy,
          priceStartAt,
          priceEndAt
        )
      );
      countService.shutdown();

      List<Product> productList = productService.getProducts(
        page,
        limit,
        sortBy,
        direction,
        searchBy,
        priceStartAt,
        priceEndAt
      );

      List<Future<ProductDTO>> list = new LinkedList<>();

      loadProducts(executorService, productList, list);

      executorService.shutdown();

      return new Pageable<>(
        countAllProducts.get(),
        list
          .stream()
          .map(f -> {
            try {
              return f.get();
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          })
          .toList()
      );
    }

    List<Category> categoryChildList = categoryService.getChildCategories(
      categoryId
    );

    List<Long> ids = new java.util.LinkedList<>(List.of(categoryId));

    ids.addAll(categoryChildList.stream().map(Category::getId).toList());

    Future<Long> countAllProducts = countService.submit(() ->
      productService.countProductsByCategoryId(
        ids,
        searchBy,
        priceStartAt,
        priceEndAt
      )
    );

    List<Future<ProductDTO>> list = new LinkedList<>();

    List<Product> productList = productService.getProductsByCategoryId(
      searchBy,
      page,
      limit,
      sortBy,
      direction,
      priceStartAt,
      priceEndAt,
      ids
    );

    loadProducts(executorService, productList, list);

    return new Pageable<>(
      countAllProducts.get(),
      list
        .stream()
        .map(f -> {
          try {
            return f.get();
          } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
          }
        })
        .toList()
    );
  }

  private void loadProducts(
    ExecutorService executorService,
    List<Product> productList,
    List<Future<ProductDTO>> list
  ) {
    for (Product f : productList) {
      Future<ProductDTO> dtoFuture = executorService.submit(() ->
        new ProductDTO(
          f.getId(),
          f.getName(),
          f.getProducer(),
          f.getPrice(),
          opinionService.getProductAverageOpinions(f.getId()),
          f.getDescription(),
          f.getCategory(),
          blobService
            .getBlobAttachments(f.getFileName())
            .stream()
            .map(Attachment::new)
            .toList(),
          f.getSpecifications()
        )
      );
      list.add(dtoFuture);
    }
  }

  @GetMapping("/producer")
  public Pageable<String> getProducersByParentCategoryId(
    @RequestParam(value = "parentCategoryId") Long id
  ) {
    List<String> producersByParentCategoryId = productService.getProducersByParentCategoryId(
      id
    );

    return new Pageable<>(
      (long) producersByParentCategoryId.size(),
      producersByParentCategoryId
    );
  }

  @PutMapping(value = "/{id}/admin")
  public void updateProduct(
    @PathVariable("id") Long id,
    @Valid @RequestBody ProductRequest productRequest
  ) {
    Product product = productService.getProductById(id);
    Category category = categoryService.getCategoryById(
      productRequest.getCategoryId()
    );

    if (category == null) throw new RuntimeException();

    product.setCategory(category);
    product.setDescription(productRequest.getDescription());
    product.setName(productRequest.getName());
    product.setProducer(productRequest.getProducer());
    product.setPrice(productRequest.getPrice());

    specificationService.removeAllSpecifications(product.getSpecifications());

    List<String> attachmentListToSave = productRequest
      .getAttachments()
      .stream()
      .map(Attachment::getBlob)
      .toList();

    blobService.uploadBlob(attachmentListToSave, product.getFileName());

    List<Specification> specificationList = productRequest
      .getSpecifications()
      .stream()
      .map(f -> new Specification(f.getKeyName(), f.getKeyValue(), product))
      .toList();

    product.getSpecifications().clear();
    product.getSpecifications().addAll(specificationList);
    log.info(specificationList.size());
    specificationService.saveAllSpecification(specificationList);

    productService.updateProduct(product);
  }

  @DeleteMapping(value = "/{id}/admin")
  public void removeProduct(@PathVariable("id") Long id) {
    Product product = productService.getProductById(id);
    blobService.removeBlob(product.getFileName());
    productService.deleteProductById(id);
  }

  @GetMapping(path = "/{id}")
  public ProductDTO getProductById(@PathVariable("id") Long id) {
    Product productById = productService.getProductById(id);

    return new ProductDTO(
      productById.getId(),
      productById.getName(),
      productById.getProducer(),
      productById.getPrice(),
      opinionService.getProductAverageOpinions(id),
      productById.getDescription(),
      productById.getCategory(),
      blobService
        .getBlobAttachments(productById.getFileName())
        .stream()
        .map(Attachment::new)
        .toList(),
      productById.getSpecifications()
    );
  }
}
