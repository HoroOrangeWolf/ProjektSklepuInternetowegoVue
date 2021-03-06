package com.computer.parts.shop.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository
  extends JpaRepository<OrderProduct, Long> {}
