package com.computer.parts.shop.Pageable;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pageable<T> {

  private Long totalCount;
  private List<T> list = new ArrayList<>();
}
