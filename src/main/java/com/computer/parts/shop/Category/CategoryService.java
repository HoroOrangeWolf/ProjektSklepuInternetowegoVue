package com.computer.parts.shop.Category;

import com.computer.parts.shop.Category.Response.CategoryDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void addCategory(Category category){
        categoryRepository.save(category);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public List<Category> getAllCategoriesById(List<Long> ids){
        return categoryRepository.findAllById(ids);
    }

    public Category getCategoryById(Long id){
        Optional<Category> byId = categoryRepository.findById(id);

        if(byId.isEmpty())
            throw new IllegalStateException("Category not found!");

        return byId.get();
    }

    public void updateCategory(Category category){
        categoryRepository.save(category);
    }

    public List<Category> getChildCategories(Long parentCategoryId){
        Category category = getCategoryById(parentCategoryId);
        return category.getAllChildIdCategories();
    }

    public List<CategoryDTO> getTreeCategoriesWithoutParent(Integer maxTreeLevel){
        List<Category> categories = categoryRepository.getCategoriesWithoutParent();
        List<CategoryDTO> categoryDTOList = new LinkedList<>();
        for (Category category : categories) {
            categoryDTOList.add(
                    category.getCategoryDTOAsTree(maxTreeLevel - 1)
            );
        }

        return categoryDTOList;
    }

    public List<CategoryDTO> getTreeCategoriesWithParent(Long parentId, Integer maxTreeLevel){
        Category category = getCategoryById(parentId);

        CategoryDTO categoryDTO = category.getCategoryDTOAsTree(maxTreeLevel - 1);

        return List.of(categoryDTO);
    }


    public void removeCategory(Long id){
        categoryRepository.deleteById(id);
    }

}
