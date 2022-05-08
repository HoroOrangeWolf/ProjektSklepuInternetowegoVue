package com.computer.parts.shop.User.Request;

import java.sql.Timestamp;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalDataUpdate {

  @Length(min = 3, max = 255)
  private String name;

  @Length(min = 3, max = 255)
  private String surname;

  @NotNull
  private Timestamp birthDay;
}
