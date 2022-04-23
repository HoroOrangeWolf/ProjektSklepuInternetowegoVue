package com.computer.parts.shop.Opinion;

import com.computer.parts.shop.Opinion.Request.OpinionRequest;
import com.computer.parts.shop.Product.ProductService;
import com.computer.parts.shop.User.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RequestMapping("/api/v1/opinion")
@RestController
@AllArgsConstructor
public class OpinionController {

    private OpinionService opinionService;
    private ProductService productService;

    @PostMapping("/user")
    public void addOpinion(@RequestBody OpinionRequest opinionRequest, Authentication authentication){

        User user = (User) authentication.getPrincipal();

        opinionService.addOpinion(new Opinion(
                opinionRequest.getStars(),
                opinionRequest.getText(),
                productService.getProductById(opinionRequest.getProductId()),
                user
        ));
    }

    @GetMapping
    public Map<String, Object> getOpinions(
            @RequestParam(
                    value = "opinionsFor"
            )
            OpinionsFor opinionsFor,
            @RequestParam(
                    value = "id"
            )
            Long id
    ){
        Map<String, Object> stringObjectMap = new TreeMap<>();
        List<Opinion> opinionList = null;

        switch (opinionsFor){
            case USER:
                opinionList = opinionService.getOpinionsByUserId(id);
                stringObjectMap.put("totalCount", opinionList.size());
                stringObjectMap.put("list", opinionList.stream().map(f->{
                    Map<String, Object> objectObjectTreeMap = new TreeMap<>();

                    objectObjectTreeMap.put("text", f.getText());
                    objectObjectTreeMap.put("stars", f.getStars());
                    objectObjectTreeMap.put("productId", f.getProduct().getId());

                    return objectObjectTreeMap;
                }).toList());
                break;
            case PRODUCT:
                opinionList = opinionService.getOpinionsByProductId(id);
                stringObjectMap.put("totalCount", opinionList.size());
                stringObjectMap.put("avgStars", opinionService.getProductAverageOpinions(id));
                stringObjectMap.put("list", opinionList.stream().map(f->{
                    Map<String, Object> objectObjectTreeMap = new TreeMap<>();

                    objectObjectTreeMap.put("text", f.getText());
                    objectObjectTreeMap.put("stars", f.getStars());
                    objectObjectTreeMap.put("productId", f.getProduct().getId());
                    objectObjectTreeMap.put("userId", f.getUser().getId());

                    return objectObjectTreeMap;
                }).toList());
                break;
        }
        return null;
    }





}
