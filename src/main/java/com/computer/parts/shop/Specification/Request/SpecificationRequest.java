package com.computer.parts.shop.Specification.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpecificationRequest {
    private Long id = null;
    private String keyName;
    private String keyValue;
}
