package com.computer.parts.shop.Opinion.Request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpinionRequest {

  @Min(value = 1)
  @Max(value = 5)
  private Short stars;

  @Size(min = 1, max = 2048)
  private String text;

  @NotNull
  private Long productId;
}
