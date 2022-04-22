package com.computer.parts.shop.Product.Opinion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Long> {


    @Query("SELECT c FROM Opinion c WHERE c.product.id = ?1")
    List<Opinion> getProductOpinions(Long productId);


}
