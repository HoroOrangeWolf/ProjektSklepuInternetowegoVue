package com.computer.parts.shop.Order.Requests;

import com.computer.parts.shop.Order.DeliveryType;
import com.computer.parts.shop.Order.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequest {

    private PaymentType paymentType;

    private DeliveryType deliveryType;

    private String remarks;

    private List<ProductRequestOrder> productRequestList;
}
