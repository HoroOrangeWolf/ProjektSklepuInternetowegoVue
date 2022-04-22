package com.computer.parts.shop.Product.Opinion;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class OpinionService {

    private OpinionRepository opinionRepository;


}
