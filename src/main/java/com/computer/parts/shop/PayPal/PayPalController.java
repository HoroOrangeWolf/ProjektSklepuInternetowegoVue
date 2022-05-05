package com.computer.parts.shop.PayPal;

import com.computer.parts.shop.PayPal.Request.PaymentRequest;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/paypal")
@AllArgsConstructor
public class PayPalController {

  private PayPalService payPalService;
  private static final String SUCCESS_URL = "pay/success";
  private static final String CANCEL_URL = "pay/cancel";
  private static final String BASE_URL = "http://localhost:8080/";

  @PostMapping
  public ResponseEntity<String> payment(
    @RequestBody PaymentRequest paymentRequest
  ) throws PayPalRESTException {
    Payment payment = payPalService.createPayment(
      paymentRequest.getPrice(),
      paymentRequest.getCurrency(),
      paymentRequest.getMethod(),
      paymentRequest.getIntent(),
      paymentRequest.getDescription(),
      BASE_URL + CANCEL_URL,
      BASE_URL + SUCCESS_URL
    );

    Optional<Links> approval_url = payment
      .getLinks()
      .stream()
      .filter(f -> f.getRel().contains("approval_url"))
      .findFirst();

    if (approval_url.isEmpty()) return ResponseEntity.notFound().build();

    return ResponseEntity.ok(approval_url.get().getHref());
  }

  @GetMapping(value = SUCCESS_URL)
  public ResponseEntity<String> successPay(
    @RequestParam("paymentId") String paymentId,
    @RequestParam("PayerID") String PayerId
  ) throws PayPalRESTException {
    Payment payment = payPalService.executePayment(paymentId, PayerId);

    return ResponseEntity.ok(
      payment.getState().equals("approved") ? "success" : "fail"
    );
  }
}
