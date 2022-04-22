package com.computer.parts.shop.Product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Query("SELECT COUNT(c) as productCount FROM Product c LEFT OUTER JOIN c.category cat WHERE c.category.id IN ?1 AND (UPPER(c.name) LIKE CONCAT(UPPER(?2), '%') OR UPPER(c.producer) LIKE CONCAT(UPPER(?2), '%') OR UPPER(cat.name) LIKE CONCAT(UPPER(?2), '%'))")
    Long getProductCountByCategoryIds(List<Long> ids, String searchBy);

    @Query("SELECT c FROM Product c LEFT OUTER JOIN c.category cat WHERE c.category.id IN ?1 AND (UPPER(c.name) LIKE CONCAT(UPPER(?2), '%') OR UPPER(c.producer) LIKE CONCAT(UPPER(?2), '%') OR UPPER(cat.name) LIKE CONCAT(UPPER(?2), '%'))")
    List<Product> getProductsByCategoryId(List<Long> categoryId,String searchBy ,Pageable pageable);

    @Query("SELECT c FROM Product c LEFT OUTER JOIN c.category cat WHERE UPPER(c.name) LIKE CONCAT(UPPER(?1), '%') OR UPPER(c.producer) LIKE CONCAT(UPPER(?1), '%') OR UPPER(cat.name) LIKE CONCAT(UPPER(?1), '%')")
    List<Product> getProductsAlikeToSearchBy(String searchBy, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Product c LEFT OUTER JOIN c.category cat WHERE UPPER(c.name) LIKE CONCAT(UPPER(?1), '%') OR UPPER(c.producer) LIKE CONCAT(UPPER(?1), '%') OR UPPER(cat.name) LIKE CONCAT(UPPER(?1), '%')")
    Long countProductsAlikeToSearchBy(String searchBy);

}
