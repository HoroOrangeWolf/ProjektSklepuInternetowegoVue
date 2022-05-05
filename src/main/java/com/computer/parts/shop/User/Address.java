package com.computer.parts.shop.User;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
  @SequenceGenerator(name = "address_seq")
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "post_code", nullable = false)
  private String postCode;

  @Column(name = "house_number", nullable = false)
  private String homeNumber;

  @Column(name = "street")
  private String street;

  @Column(name = "city")
  private String city;

  public Address(
    String postCode,
    String houseNumber,
    String street,
    String city
  ) {
    this.postCode = postCode;
    this.homeNumber = houseNumber;
    this.street = street;
    this.city = city;
  }
}
