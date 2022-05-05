package com.computer.parts.shop.Product.Response;

import com.computer.parts.shop.Attachment.Attachment;
import com.computer.parts.shop.Category.Category;
import com.computer.parts.shop.Product.Product;
import com.computer.parts.shop.Specification.Specification;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {

  private Long id;

  private String name;

  private String producer;

  private BigDecimal price;

  private Double avgOpinion;

  private String description;

  private Category category;

  private List<Attachment> attachments = new ArrayList<>();

  private List<Specification> specifications = new ArrayList<>();
}
