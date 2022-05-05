package com.computer.parts.shop.Opinion;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Long> {
  @Query("SELECT c FROM Opinion c WHERE c.product.id = ?1")
  List<Opinion> getAllProductOpinions(Long productId, Pageable pageable);

  @Query("SELECT COUNT(c) FROM Opinion c WHERE c.user.id = ?1")
  Long countAllUserOpinions(Long userId);

  @Query("SELECT c FROM Opinion c WHERE c.user.id = ?1")
  List<Opinion> getUserOpinions(Long userId, Pageable pageable);

  @Query("SELECT AVG(c.stars) FROM Opinion c WHERE c.product.id = ?1")
  Double getProductAvgStars(Long productId);

  @Query(
    "SELECT COUNT(c) FROM Opinion c WHERE c.user.id = ?1 AND c.product.id = ?2"
  )
  Long countAllUserOpinionsByUserIdAndProductId(Long userID, Long productId);
}
