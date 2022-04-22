package com.computer.parts.shop.Specification;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
@AllArgsConstructor
public class SpecificationService {
    private SpecificationRepository specificationRepository;

    public void saveAllSpecification(List<Specification> specificationList){
        specificationRepository.saveAll(specificationList);
    }

    public void removeAllSpecifications(List<Specification> specifications){
        specificationRepository.deleteAll(specifications);
    }

}
