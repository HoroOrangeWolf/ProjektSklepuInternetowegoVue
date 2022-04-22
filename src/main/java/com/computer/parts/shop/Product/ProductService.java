package com.computer.parts.shop.Product;

import com.computer.parts.shop.Category.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;
    private CategoryService categoryService;

    public void addProduct(Product product){
        productRepository.save(product);
    }

    public void updateProduct(Product product){
        boolean isExist = productRepository.existsById(product.getId());

        if(!isExist)
            throw new IllegalStateException("Product not found");

        productRepository.save(product);
    }

    public Product getProductById(Long id){
        Optional<Product> byId = productRepository.findById(id);
        if(byId.isEmpty())
            throw new IllegalStateException("Product not found");

        return byId.get();
    }

    public void deleteProductById(Long id){
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByCategoryId(String searchBy ,Integer page, Integer limit, String sortBy, Direction direction, List<Long> ids){

        Order order = Order
                .by(sortBy)
                .with(direction);

        Sort sort = Sort.by(
                List.of(
                        order
                )
        );

        Pageable pageable = PageRequest.of(
                Math.toIntExact(page),
                Math.toIntExact(limit),
                sort
        );

        return productRepository.getProductsByCategoryId(ids, searchBy,pageable);
    }

    public List<Product> getProducts(Integer page,Integer limit, String sortBy, Direction direction, String searchBy){
        Order order = Order
                .by(sortBy)
                .with(direction);

        Sort sort = Sort.by(
                List.of(
                        order
                )
        );

        Pageable pageable = PageRequest.of(
                Math.toIntExact(page),
                Math.toIntExact(limit),
                sort
        );

        return productRepository.getProductsAlikeToSearchBy(searchBy, pageable);
    }

    public Long countAllProductsAlikeToSearchBy(String searchBy){
        return productRepository.countProductsAlikeToSearchBy(searchBy);
    }

    public Long countProductsByCategoryId(List<Long> ids, String searchBy){
        return productRepository.getProductCountByCategoryIds(ids, searchBy);
    }


}
