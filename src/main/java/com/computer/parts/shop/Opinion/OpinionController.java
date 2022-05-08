package com.computer.parts.shop.Opinion;

import com.computer.parts.shop.Opinion.Request.OpinionRequest;
import com.computer.parts.shop.Product.ProductService;
import com.computer.parts.shop.User.User;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/opinion")
@RestController
@AllArgsConstructor
public class OpinionController {

  private OpinionService opinionService;
  private ProductService productService;

  @PostMapping("/user")
  public void addOpinion(
    @Valid @RequestBody OpinionRequest opinionRequest,
    Authentication authentication
  ) {
    User user = (User) authentication.getPrincipal();

    opinionService.addOpinion(
      new Opinion(
        opinionRequest.getStars(),
        opinionRequest.getText(),
        productService.getProductById(opinionRequest.getProductId()),
        user
      )
    );
  }

  @GetMapping
  public Map<String, Object> getOpinions(
    @RequestParam(value = "opinionsFor") OpinionsFor opinionsFor,
    @RequestParam(value = "id", defaultValue = "-1", required = false) Long id,
    @RequestParam(
      value = "page",
      required = false,
      defaultValue = "0"
    ) Integer page,
    @RequestParam(
      value = "limit",
      required = false,
      defaultValue = "20"
    ) Integer limit,
    @RequestParam(
      value = "sortBy",
      required = false,
      defaultValue = "id"
    ) String sortBy,
    @RequestParam(
      value = "sort",
      required = false,
      defaultValue = "DESC"
    ) Sort.Direction direction,
    Authentication authentication
  ) {
    Map<String, Object> stringObjectMap = new TreeMap<>();
    List<Opinion> opinionList = null;

    switch (opinionsFor) {
      case USER -> {
        User user = (User) authentication.getPrincipal();
        opinionList =
          opinionService.getOpinionsByUserId(
            user.getId(),
            page,
            limit,
            sortBy,
            direction
          );
        stringObjectMap.put(
          "totalCount",
          opinionService.countOpinionsByUserId(id)
        );
        opinionMapper(stringObjectMap, opinionList);
      }
      case PRODUCT -> {
        opinionList =
          opinionService.getOpinionsByProductId(
            id,
            page,
            limit,
            sortBy,
            direction
          );
        stringObjectMap.put(
          "totalCount",
          opinionService.countOpinionsByProductId(id)
        );
        stringObjectMap.put(
          "avgStars",
          opinionService.getProductAverageOpinions(id)
        );
        opinionMapper(stringObjectMap, opinionList);
      }
    }
    return stringObjectMap;
  }

  private void opinionMapper(
    Map<String, Object> stringObjectMap,
    List<Opinion> opinionList
  ) {
    stringObjectMap.put(
      "list",
      opinionList
        .stream()
        .map(f -> {
          Map<String, Object> objectObjectTreeMap = new TreeMap<>();
          objectObjectTreeMap.put("text", f.getText());
          objectObjectTreeMap.put("stars", f.getStars());
          objectObjectTreeMap.put("userName", f.getUser().getName());
          objectObjectTreeMap.put("productName", f.getProduct().getName());
          objectObjectTreeMap.put("creationTime", f.getCreationTime());
          objectObjectTreeMap.put("productId", f.getProduct().getId());
          return objectObjectTreeMap;
        })
        .toList()
    );
  }
}
