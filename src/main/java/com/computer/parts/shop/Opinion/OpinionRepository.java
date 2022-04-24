package com.computer.parts.shop.Opinion;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Long> {


    @Query("SELECT c FROM Opinion c WHERE c.product.id = ?1")
    List<Opinion> getAllProductOpinions(Long productId, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Opinion c WHERE c.product.id = ?1")
    Long countAllProductOpinions(Long productId);

    @Query("SELECT COUNT(c) FROM Opinion c WHERE c.user.id = ?1")
    Long countAllUserOpinions(Long userId);

    @Query("SELECT c FROM Opinion c WHERE c.user.id = ?1")
    List<Opinion> getUserOpinions(Long userId, Pageable pageable);

    @Query("SELECT AVG(c.stars) FROM Opinion c WHERE c.product.id = ?1")
    Double getProductAvgStars(Long productId);


}
