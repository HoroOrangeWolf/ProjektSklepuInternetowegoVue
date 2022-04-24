package com.computer.parts.shop.Opinion;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<Opinion> getOpinionsByUserId(Long userId, Integer page, Integer limit, String sortBy, Sort.Direction direction){

        Sort.Order order = Sort.Order
                .by(sortBy)
                .with(direction);

        Sort sort = Sort.by(
                List.of(
                        order
                )
        );

        Pageable pageable = PageRequest.of(
                page,
                limit,
                sort
        );

        return opinionRepository.getUserOpinions(userId, pageable);
    }

    public Long countOpinionsByUserId(Long userId){
        return opinionRepository.countAllUserOpinions(userId);
    }

    public List<Opinion> getOpinionsByProductId(Long productId, Integer page, Integer limit, String sortBy, Sort.Direction direction){

        Sort.Order order = Sort.Order
                .by(sortBy)
                .with(direction);

        Sort sort = Sort.by(
                List.of(
                        order
                )
        );

        Pageable pageable = PageRequest.of(
                page,
                limit,
                sort
        );

        return opinionRepository.getAllProductOpinions(productId, pageable);
    }

    public Long countOpinionsByProductId(Long productId){
        return opinionRepository.countAllUserOpinions(productId);
    }

    public Double getProductAverageOpinions(Long productId){
        return opinionRepository.getProductAvgStars(productId);
    }
}
