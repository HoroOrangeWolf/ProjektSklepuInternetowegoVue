package com.computer.parts.shop.Category.Response;

import com.computer.parts.shop.Category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryDTO {

    private Long id;
    private String name;
    private List<CategoryDTO> categoryList = new ArrayList<>();

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
