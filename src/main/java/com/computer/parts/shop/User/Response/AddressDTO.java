package com.computer.parts.shop.User.Response;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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

  @Pattern(regexp = "[0-9]{2}-[0-9]{3}")
  private String postCode;

  @Size(min = 1, max = 10)
  private String homeNumber;

  @Size(min = 2, max = 255)
  private String street;

  @Size(min = 2, max = 255)
  private String city;
}
