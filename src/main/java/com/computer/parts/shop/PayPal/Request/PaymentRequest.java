package com.computer.parts.shop.PayPal.Request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentRequest {
    private String currency;
    private String method;
    private String intent;
    private String description;
    private Double price;
}
