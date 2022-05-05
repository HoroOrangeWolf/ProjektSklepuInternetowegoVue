package com.computer.parts.shop.Product.Request;

import com.computer.parts.shop.Attachment.Attachment;
import com.computer.parts.shop.Specification.Request.SpecificationRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductRequest {

  private Long id;

  @Size(min = 3, max = 255)
  private String name;

  @Size(min = 3, max = 255)
  private String producer;

  @DecimalMin(value = "0.01")
  private BigDecimal price;

  @Size(min = 5, max = 2048)
  private String description;

  @NotNull
  private Long categoryId;

  @Size(min = 1)
  private List<SpecificationRequest> specifications;

  @Size(min = 1)
  private List<Attachment> attachments = new ArrayList<>();
}
