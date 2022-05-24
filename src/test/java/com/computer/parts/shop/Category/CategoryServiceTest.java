package com.computer.parts.shop.Category;

import com.computer.parts.shop.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    private void setUp(){
        categoryService = new CategoryService(
                categoryRepository
        );
    }

    @Test
    void getAllCategories() {
        //given

        //when
        categoryService.getAllCategories();
        //then
        verify(categoryRepository).findAll();
    }


    @Test
    void removeCategory() {
       //given
        Long id = 1L;
        //when
        categoryService.removeCategory(id);
        //then
        verify(categoryRepository).deleteById(id);
    }

    @Test
    void getChildCategoriesWithoutParent(){
        //given
        Category category = new Category(1L, "Parent");
        Category child = new Category(2L, "Child");

        category.setCategoryList(List.of(child));

        //when
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        List<Category> childCategories = categoryService.getChildCategories(1L);
        //then
        assertArrayEquals(new Category[]{child},childCategories.toArray(new Category[0]));
    }
}