package com.computer.parts.shop.Registarion;

import com.computer.parts.shop.User.Gender;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationRequest {

  @Length(min = 6, max = 30)
  private String password;

  @Email
  private String email;

  @NotNull
  private Gender gender;

  private String phoneNumber;

  @Length(min = 3, max = 255)
  private String name;

  @Length(min = 3, max = 255)
  private String surname;

  @NotNull
  private Timestamp birthDay;

  @Pattern(regexp = "[0-9]{2}-[0-9]{3}")
  private String postCode;

  @Size(min = 1, max = 10)
  private String homeNumber;

  @Size(min = 2, max = 255)
  private String street;

  @Size(min = 2, max = 255)
  private String city;
}
