package com.computer.parts.shop.Order.Requests;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderResponse {

  private BigDecimal totalPrice;
  private String paymentLink;
}
