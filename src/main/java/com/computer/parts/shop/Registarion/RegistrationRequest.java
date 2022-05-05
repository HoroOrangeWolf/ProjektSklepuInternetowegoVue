package com.computer.parts.shop.Registarion;

import com.computer.parts.shop.User.Gender;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationRequest {

  @Length(min = 3, max = 12)
  private String login;

  @Length(min = 3, max = 12)
  private String password;

  @Email
  @NotEmpty
  @NotNull
  private String email;

  private Gender gender;

  private String phoneNumber;

  @Length(min = 3, max = 12)
  private String name;

  @Length(min = 3, max = 12)
  private String surname;

  private Timestamp birthDay;

  private String postCode;

  private String homeNumber;

  private String street;

  private String city;
}
