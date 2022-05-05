package com.computer.parts.shop.Order;

import java.util.List;
import java.util.Optional;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository
  extends PagingAndSortingRepository<Order, Long> {
  @Query("SELECT c FROM Order c INNER JOIN account u ON c.user.id=u.id")
  List<Order> getUserOrdersById(Long id, Pageable pageable);

  @Query("SELECT c FROM Order  c WHERE c.paymentId LIKE ?1")
  Optional<Order> getOrderByPaymentId(String paymentId);

  @Query("SELECT c FROM Order c WHERE c.user.id = ?1")
  List<Order> getOrdersByUserId(Long id, Pageable pageable);

  @Query("SELECT COUNT(c) FROM Order c WHERE c.user.id = ?1")
  Long countOrderByUserId(Long id);

  @Query(
    "SELECT c FROM Order c WHERE UPPER(c.user.email) LIKE CONCAT(UPPER(?1), '%')"
  )
  List<Order> getOrdersByUserEmail(String email, Pageable pageable);

  @Query(
    "SELECT COUNT(c) FROM Order c WHERE UPPER(c.user.email) LIKE CONCAT(UPPER(?1), '%')"
  )
  Long countOrdersByUserEmail(String email);
}
