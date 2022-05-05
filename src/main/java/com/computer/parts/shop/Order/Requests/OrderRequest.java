package com.computer.parts.shop.Order.Requests;

import com.computer.parts.shop.Order.DeliveryType;
import com.computer.parts.shop.Order.PaymentType;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequest {

  @NotNull
  private PaymentType paymentType;

  @NotNull
  private DeliveryType deliveryType;

  private String remarks;

  @Size(min = 1)
  private List<ProductRequestOrder> productRequestList;
}
