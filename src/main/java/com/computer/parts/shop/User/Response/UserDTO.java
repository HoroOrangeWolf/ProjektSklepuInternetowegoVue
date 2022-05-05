package com.computer.parts.shop.User.Response;

import com.computer.parts.shop.User.Gender;
import com.computer.parts.shop.User.Role;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

  private String name;
  private String surname;
  private String email;
  private Role role;
  private Boolean isAuthenticated;
  private Boolean isSocialAccount;
  private AddressDTO address;
  private Timestamp birthDay;
  private Gender gender;
}
