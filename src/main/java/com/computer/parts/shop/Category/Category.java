package com.computer.parts.shop.Category;

import com.computer.parts.shop.Category.Response.CategoryDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_generator_sequence")
    @SequenceGenerator(name = "category_generator_sequence", sequenceName = "category_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category = null;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    @BatchSize(size = 20)
    private List<Category> categoryList = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(Long id, String name, Category category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    @Transient
    public List<Category> getAllChildIdCategories(){
        return getAllChildCategories(this);
    }


    @Transient
    public CategoryDTO getCategoryDTOAsTree(Integer maxTreeLevel){
        return getCategoryDTOAsTree(this, 0, maxTreeLevel);
    }

    @Transient
    private CategoryDTO getCategoryDTOAsTree(Category category, Integer current, Integer maxTreeLevel){

        CategoryDTO categoryDTO = new CategoryDTO(
                category.getId(),
                category.getName());

        if(current.equals(maxTreeLevel))
            return categoryDTO;

        List<CategoryDTO> categoryDTOList = new LinkedList<>();

        for(Category categoryBuff : category.categoryList){
            CategoryDTO categoryDTOAsTree = categoryBuff.getCategoryDTOAsTree(categoryBuff, current, maxTreeLevel);
            categoryDTOList.add(categoryDTOAsTree);
        }

        categoryDTO.setCategoryList(
                categoryDTOList
        );

        return categoryDTO;
    }

    @Transient
    private List<Category> getAllChildCategories(Category category){
        var childCategories = new LinkedList<Category>();

        for(Category singleCategory : category.categoryList)
        {
            childCategories.add(singleCategory);
            var singleCategoriesChildren = getAllChildCategories(singleCategory);
            childCategories.addAll(singleCategoriesChildren);
        }
        return childCategories;
    }


    @Override
    @Transient
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return id != null && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}