package com.computer.parts.shop.Category;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  @Query("SELECT c FROM Category c WHERE c.category IS NULL")
  List<Category> getCategoriesWithoutParent();
}
