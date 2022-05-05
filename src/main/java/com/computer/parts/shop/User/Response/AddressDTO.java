package com.computer.parts.shop.User.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressDTO {

  private Long id;
  private String postCode;
  private String homeNumber;
  private String street;
  private String city;
}
