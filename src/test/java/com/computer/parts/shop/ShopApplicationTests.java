package com.computer.parts.shop;

import com.computer.parts.shop.Order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DataJpaTest
class ShopApplicationTests {

  @Autowired
  private OrderRepository service;

  @Test
  void contextLoads() {
    Sort sort = Sort.by("id").descending();
    Pageable pageable = PageRequest.of(1, 1, sort);
    service.getUserOrdersById(1L, pageable);
  }
}
