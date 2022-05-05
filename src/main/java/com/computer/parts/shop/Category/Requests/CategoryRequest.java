package com.computer.parts.shop.Category.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

  private Long id;
  private String name;
  private Long parentCategoryId = null;
}
