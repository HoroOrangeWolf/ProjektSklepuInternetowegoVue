package com.computer.parts.shop.PayPal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class PayPalService {

  private APIContext apiContext;

  public Payment createPayment(
    Double total,
    String currency,
    String method,
    String intent,
    String description,
    String cancelUrl,
    String successUrl
  ) throws PayPalRESTException {
    total =
      new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();

    Amount amount = new Amount(
      currency,
      String.format("%.2f", total).replace(",", ".")
    );
    Transaction transaction = new Transaction();

    transaction.setAmount(amount);
    transaction.setDescription(description);

    List<Transaction> list = List.of(transaction);

    Payer payer = new Payer();

    payer.setPaymentMethod(method);

    Payment payment = new Payment();

    payment.setIntent(intent);
    payment.setPayer(payer);
    payment.setTransactions(list);
    RedirectUrls redirectUrls = new RedirectUrls();
    redirectUrls.setReturnUrl(successUrl);
    redirectUrls.setCancelUrl(cancelUrl);
    payment.setRedirectUrls(redirectUrls);
    return payment.create(apiContext);
  }

  public Payment executePayment(String paymentId, String payerId)
    throws PayPalRESTException {
    Payment payment = new Payment();
    payment.setId(paymentId);
    PaymentExecution paymentExecution = new PaymentExecution();

    paymentExecution.setPayerId(payerId);

    return payment.execute(apiContext, paymentExecution);
  }
}
