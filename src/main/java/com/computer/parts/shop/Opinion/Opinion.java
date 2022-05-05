package com.computer.parts.shop.Opinion;

import com.computer.parts.shop.Order.Order;
import com.computer.parts.shop.Product.Product;
import com.computer.parts.shop.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "opinion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Opinion {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "opinion_seq")
  @SequenceGenerator(name = "opinion_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "stars", nullable = false)
  private Short stars;

  @Column(name = "text", length = 2048)
  private String text;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private User user;

  @Column(name = "creation_time")
  private Timestamp creationTime = new Timestamp(new Date().getTime());

  public Opinion(Short stars, String text, Product product, User user) {
    this.stars = stars;
    this.text = text;
    this.product = product;
    this.user = user;
  }
}
