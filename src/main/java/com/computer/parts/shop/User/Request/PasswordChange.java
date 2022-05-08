package com.computer.parts.shop.User.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChange {

  @Length(min = 6, max = 30)
  private String newPassword;
}
