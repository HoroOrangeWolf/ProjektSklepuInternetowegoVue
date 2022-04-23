package com.computer.parts.shop.Opinion.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpinionRequest {

    private Short stars;
    private String text;
    private Long productId;

}
