package com.computer.parts.shop.Product.Request;

import com.computer.parts.shop.Attachment.Attachment;
import com.computer.parts.shop.Specification.Request.SpecificationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductRequest {

    private Long id;

    private String name;

    private String producer;

    private BigDecimal price;

    private Integer count;

    private String description;

    private Long categoryId;

    private List<SpecificationRequest> specifications;

    private List<Attachment> attachments = new ArrayList<>();
}
