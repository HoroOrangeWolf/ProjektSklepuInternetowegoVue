package com.computer.parts.shop.Opinion;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class OpinionService {

    private OpinionRepository opinionRepository;

    public void addOpinion(Opinion opinion){
        opinionRepository.save(opinion);
    }

    public List<Opinion> getOpinionsByUserId(Long userId){
        return opinionRepository.getUserOpinions(userId);
    }

    public List<Opinion> getOpinionsByProductId(Long productId){
        return opinionRepository.getProductOpinions(productId);
    }

    public Double getProductAverageOpinions(Long productId){
        return opinionRepository.getProductAvgStars(productId);
    }
}
