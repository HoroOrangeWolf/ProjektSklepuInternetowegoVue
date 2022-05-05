package com.computer.parts.shop.Order;

import com.computer.parts.shop.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "ordered")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
  @SequenceGenerator(name = "order_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "total_price", precision = 19, scale = 2)
  private BigDecimal totalPrice;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_status", nullable = false)
  private PaymentStatus paymentStatus = PaymentStatus.WAITING_FOR_PAYMENT;

  @Enumerated(EnumType.STRING)
  @Column(name = "shipment_status", nullable = false)
  private ShipmentStatus shipmentStatus = ShipmentStatus.IN_PREPARATION;

  @Enumerated(EnumType.STRING)
  @Column(name = "delivery_type")
  private DeliveryType deliveryType;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_type", nullable = false)
  private PaymentType paymentType;

  @Column(name = "remarks", length = 512)
  private String remarks;

  @Column(name = "payer_id")
  @JsonIgnore
  private String payerId;

  @Column(name = "payment_id")
  @JsonIgnore
  private String paymentId;

  @Column(name = "pay_link")
  private String payLink;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User user;

  @OneToMany(mappedBy = "order", orphanRemoval = true)
  @LazyCollection(LazyCollectionOption.FALSE)
  private List<OrderProduct> orderProducts = new ArrayList<>();

  @Column(name = "date", nullable = false)
  private Timestamp date = Timestamp.from(Instant.now());
}
