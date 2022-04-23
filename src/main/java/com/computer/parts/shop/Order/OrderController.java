package com.computer.parts.shop.Order;

import com.computer.parts.shop.Order.Requests.OrderRequest;
import com.computer.parts.shop.Order.Requests.OrderResponse;
import com.computer.parts.shop.Pageable.Pageable;
import com.computer.parts.shop.User.User;
import com.computer.parts.shop.User.UserRepository;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping(path = "api/v1/order")
@AllArgsConstructor
@Log4j2
public class OrderController {

    private OrderService orderService;
    private UserRepository userRepository;
    private static final String BASE_URL = "http://localhost:8081/api/v1/order";
    private static final String SUCCESS_LINK = "/pay/success";
    private static final String CANCEL_LINK = "/pay/cancel";

    //testowanie działa
    @PostMapping
    public ResponseEntity<OrderResponse> addOrder(@RequestBody OrderRequest orderRequest, Authentication authentication) throws PayPalRESTException {

        User user = (User) authentication.getPrincipal();

        return orderService.addOrder(orderRequest,
                user,
                BASE_URL + SUCCESS_LINK,
                BASE_URL + CANCEL_LINK);
    }

    @PutMapping("/{orderId}/admin")
    public ResponseEntity<?> changeOrderStatus(@PathVariable("orderId") Long orderId,
                                  @RequestParam(value = "whichStatus") String whichStatus,
                                  @RequestParam(value = "status") String status
                                  ){

        if(Objects.equals(whichStatus, "PAYMENT")){
            orderService.changePaymentStatus(orderId, PaymentStatus.valueOf(status));
        }else if(Objects.equals(whichStatus, "SHIPMENT")){
            orderService.changeShipmentStatus(orderId, ShipmentStatus.valueOf(status));
        }else{
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping(SUCCESS_LINK)
    public void  onSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String PayerId, HttpServletResponse response) throws PayPalRESTException, IOException {
        orderService.onPayPalPaymentSuccess(paymentId, PayerId);
        response.sendRedirect("http://localhost:8080/user/order/status");
    }

    @GetMapping(CANCEL_LINK)
    public void onCancel(@RequestParam("paymentId") String paymentId, HttpServletResponse response) throws IOException {
        orderService.onPayPalCancel(paymentId);
        response.sendRedirect("http://localhost:8080/user/order/status?error");
    }


    @GetMapping
    public Pageable<Order> getUserOrders(
            Authentication authentication,
            @RequestParam(
                    value = "page",
                    defaultValue = "0",
                    required = false
            )
            Integer page,
            @RequestParam(
                    value = "limit",
                    defaultValue = "20",
                    required = false
            )
            Integer limit
    ){
        User user = (User) authentication.getPrincipal();

        return new Pageable<>(
                orderService.countOrdersByUserId(user.getId()),
                orderService.getOrdersByUserId(user.getId(), page, limit)
        );
    }

    @GetMapping("/admin")
    public Pageable<Map<String, Object>> getOrders(
            @RequestParam(
                    value = "page",
                    defaultValue = "0",
                    required = false
            )
            Integer page,
            @RequestParam(
                    value = "limit",
                    defaultValue = "20",
                    required = false
            )
            Integer limit,
            @RequestParam(
                    value = "searchBy",
                    defaultValue = "",
                    required = false
            )
            String searchBy,
            @RequestParam(
                    value = "sortBy",
                    defaultValue = "id",
                    required = false
            )
            String sortBy,
            @RequestParam(
                    value = "order",
                    defaultValue = "ASC",
                    required = false
            )
            Sort.Direction direction

    ) throws ExecutionException, InterruptedException {
        Long orderId = null;

        try{
            orderId = Long.parseLong(searchBy);
        }catch (Exception exc){

        }

        if(orderId == null){
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Long> submit = executorService.submit(() -> orderService.countOrderByUserEmail(searchBy));

            List<Order> ordersByUserEmail = orderService.getOrdersByUserEmail(searchBy, page, limit, sortBy, direction);

            return new Pageable<>(submit.get(),

                    ordersByUserEmail.stream().map(f->{
               Map<String, Object> map = new TreeMap<>();
               map.put("id", f.getId());
               map.put("email", f.getUser().getEmail());
               map.put("remarks", f.getRemarks());
               map.put("paymentStatus", f.getPaymentStatus());
               map.put("shipmentStatus", f.getShipmentStatus());
               map.put("paymentType", f.getPaymentType());
               map.put("totalPrice",  f.getTotalPrice());
               return map;
            }).toList());
        }

        Optional<Order> orderById = orderService.getOrderById(orderId);

        if(orderById.isEmpty()){
            return new Pageable<>(0L, List.of());
        }

        Order order = orderById.get();

        Map<String, Object> stringObjectMap = new TreeMap<>();

        stringObjectMap.put("id", order.getId());
        stringObjectMap.put("email", order.getUser().getEmail());
        stringObjectMap.put("paymentStatus", order.getPaymentStatus());
        stringObjectMap.put("remarks", order.getRemarks());
        stringObjectMap.put("shipmentStatus", order.getShipmentStatus());
        stringObjectMap.put("totalPrice",  order.getTotalPrice());

        return new Pageable<>(
                1L,
                List.of(stringObjectMap)
        );
    }
}
