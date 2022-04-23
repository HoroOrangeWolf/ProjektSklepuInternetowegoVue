package com.computer.parts.shop.Order;

import com.computer.parts.shop.Order.Requests.OrderRequest;
import com.computer.parts.shop.Order.Requests.OrderResponse;
import com.computer.parts.shop.Order.Requests.ProductRequestOrder;
import com.computer.parts.shop.PayPal.PayPalService;
import com.computer.parts.shop.Product.Product;
import com.computer.parts.shop.Product.ProductRepository;
import com.computer.parts.shop.User.User;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class OrderService {

    private ProductRepository productRepository;
    private PayPalService payPalService;
    private OrderProductRepository orderProductRepository;
    private OrderRepository orderRepository;

    public ResponseEntity<OrderResponse> addOrder(OrderRequest orderRequest, User user, String paymentSuccessLink, String paymentCancelLink) throws PayPalRESTException {

        Order order = new Order();

        Iterable<Product> iterable = productRepository.findAllById(orderRequest.getProductRequestList().stream().map(ProductRequestOrder::getId).toList());

        List<Product> productList = new LinkedList<>();

        iterable.forEach(productList::add);

        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.valueOf(0.f));

        orderRequest.getProductRequestList().forEach(ob->{
                Product product = productList
                        .stream()
                        .filter(
                                f->f.getId().equals(ob.getId())
                        ).findFirst()
                        .orElseThrow(()->new IllegalStateException("Product not found!"));

                totalPrice.set(totalPrice.get().add(product.getPrice().multiply(BigDecimal.valueOf(ob.getCount()))));
        });


        order.setUser(user);
        order.setTotalPrice(totalPrice.get());
        order.setRemarks(orderRequest.getRemarks());
        order.setPaymentType(orderRequest.getPaymentType());
        order.setDeliveryType(orderRequest.getDeliveryType());

        List<OrderProduct> orderProducts = productList.stream().map(pro->{
            ProductRequestOrder productRequest = orderRequest.getProductRequestList()
                    .stream()
                    .filter(
                            f->f.getId().equals(pro.getId())
                    ).findFirst()
                    .orElseThrow(()->new IllegalStateException("Product not found!"));

            return new OrderProduct(productRequest.getCount(), order,pro);
        }).toList();

        orderRepository.save(order);
        orderProductRepository.saveAll(orderProducts);

        if(orderRequest.getPaymentType() == PaymentType.PAYPAL){
            Payment payment = payPalService.createPayment(
                    totalPrice.get().doubleValue(),
                    "PLN",
                    "PAYPAL",
                    "sale",
                    "Sprzedaż do sklepu!",
                    paymentCancelLink,
                    paymentSuccessLink
            );

            Links approval_url = payment.getLinks().stream().filter(f -> f.getRel().contains("approval_url")).findFirst().orElseThrow();

            order.setPayLink(approval_url.getHref());
            order.setPaymentId(payment.getId());

            orderRepository.save(order);

            return ResponseEntity.ok(new OrderResponse(totalPrice.get(), approval_url.getHref()));
        }

        return ResponseEntity.ok(new OrderResponse(totalPrice.get(), null));
    }

    public void onPayPalPaymentSuccess(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = payPalService.executePayment(paymentId, payerId);

        Order order = orderRepository.getOrderByPaymentId(paymentId).orElseThrow(() -> new IllegalStateException("Order not found!"));

        if(order.getPaymentStatus()!=PaymentStatus.SUCCESS){
            PaymentStatus paymentStatus = payment.getState().equals("approved") ? PaymentStatus.SUCCESS : PaymentStatus.DENIED;

            order.setPaymentStatus(paymentStatus);
            order.setPayerId(payerId);

            orderRepository.save(order);
        }
    }

    public Optional<Order> getOrderById(Long id){
        return orderRepository.findById(id);
    }

    public Long countOrderByUserEmail(String useremail){
        return orderRepository.countOrdersByUserEmail(useremail);
    }

    public void changePaymentStatus(Long orderId, PaymentStatus paymentStatus){
        Optional<Order> byId = orderRepository.findById(orderId);

        if(byId.isEmpty())
            throw new IllegalStateException("Order not found!");

        Order order = byId.get();

        if(order.getPaymentType() != PaymentType.TRANSFER)
            throw new RuntimeException("Invalid payment type");

        order.setPaymentStatus(paymentStatus);

        orderRepository.save(order);
    }

    public void changeShipmentStatus(Long orderId, ShipmentStatus shipmentStatus){
        Optional<Order> byId = orderRepository.findById(orderId);

        if(byId.isEmpty())
            throw new IllegalStateException("Order not found!");

        Order order = byId.get();

        order.setShipmentStatus(shipmentStatus);

        orderRepository.save(order);
    }

    public List<Order> getOrdersByUserEmail(String useremail, Integer page, Integer limit, String sortBy, Sort.Direction direction){

        Sort.Order order = Sort.Order
                .by(sortBy)
                .with(direction);

        Sort sort = Sort.by(
                List.of(
                        order
                )
        );

        Pageable pageable = PageRequest.of(
                page,
                limit,
                sort
        );
        return orderRepository.getOrdersByUserEmail(useremail, pageable);
    }

    public void onPayPalCancel(String paymentId){
        Order order = orderRepository.getOrderByPaymentId(paymentId).orElseThrow(() -> new IllegalStateException("Order not found!"));

        order.setPaymentId(paymentId);

        orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(Long userId, Integer page, Integer limit){

        Sort.Order order = Sort.Order
            .by("id")
            .with(Sort.Direction.DESC);

        Sort sort = Sort.by(
            List.of(
                    order
            )
        );

        Pageable pageable = PageRequest.of(
            page,
            limit,
            sort
        );

        return orderRepository.getOrdersByUserId(userId, pageable);
    }

    public Long countOrdersByUserId(Long userId){
        return orderRepository.countOrderByUserId(userId);
    }
}
