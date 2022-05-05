package com.computer.parts.shop.Product;

import com.computer.parts.shop.Category.Category;
import com.computer.parts.shop.Category.CategoryService;
import com.computer.parts.shop.Exceptions.BadRequestException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {

  private ProductRepository productRepository;
  private CategoryService categoryService;

  public void addProduct(Product product) {
    productRepository.save(product);
  }

  public void updateProduct(Product product) {
    boolean isExist = productRepository.existsById(product.getId());

    if (!isExist) throw new BadRequestException("Product not found");

    productRepository.save(product);
  }

  public Product getProductById(Long id) {
    Optional<Product> byId = productRepository.findById(id);
    if (byId.isEmpty()) throw new BadRequestException("Product not found");

    return byId.get();
  }

  public void deleteProductById(Long id) {
    productRepository.deleteById(id);
  }

  public List<Product> getProductsByCategoryId(
    String searchBy,
    Integer page,
    Integer limit,
    String sortBy,
    Direction direction,
    BigDecimal priceStartAt,
    BigDecimal priceEndAt,
    List<Long> ids
  ) {
    Order order = Order.by(sortBy).with(direction);

    Sort sort = Sort.by(List.of(order));

    Pageable pageable = PageRequest.of(page, limit, sort);

    return productRepository.getProductsByCategoryId(
      ids,
      searchBy,
      priceStartAt,
      priceEndAt,
      pageable
    );
  }

  public List<Product> getProducts(
    Integer page,
    Integer limit,
    String sortBy,
    Direction direction,
    String searchBy,
    BigDecimal priceStartAt,
    BigDecimal priceEndAt
  ) {
    Order order = Order.by(sortBy).with(direction);

    Sort sort = Sort.by(List.of(order));

    Pageable pageable = PageRequest.of(page, limit, sort);

    return productRepository.getProductsAlikeToSearchBy(
      searchBy,
      priceStartAt,
      priceEndAt,
      pageable
    );
  }

  public List<String> getProducersByParentCategoryId(Long parentId) {
    List<Category> childCategories = categoryService.getChildCategories(
      parentId
    );
    List<Long> ids = new LinkedList<>(List.of(parentId));
    ids.addAll(childCategories.stream().map(Category::getId).toList());

    return productRepository.getProducersByCategoriesId(ids);
  }

  public Long countAllProductsAlikeToSearchBy(
    String searchBy,
    BigDecimal priceStartAt,
    BigDecimal priceEndAt
  ) {
    return productRepository.countProductsAlikeToSearchBy(
      searchBy,
      priceStartAt,
      priceEndAt
    );
  }

  public Long countProductsByCategoryId(
    List<Long> ids,
    String searchBy,
    BigDecimal priceStartAt,
    BigDecimal priceEndAt
  ) {
    return productRepository.getProductCountByCategoryIds(
      ids,
      searchBy,
      priceStartAt,
      priceEndAt
    );
  }
}
