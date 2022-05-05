package com.computer.parts.shop.Category;

import com.computer.parts.shop.BlobService.BlobService;
import com.computer.parts.shop.Category.Requests.CategoryRequest;
import com.computer.parts.shop.Category.Response.CategoryDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping(path = "/api/v1/category")
@AllArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;
  private final BlobService blobService;

  @PostMapping(path = "/admin")
  public void addCategory(@RequestBody CategoryRequest categoryRequest) {
    if (categoryRequest.getParentCategoryId() != null) {
      Category category = categoryService.getCategoryById(
        categoryRequest.getParentCategoryId()
      );

      Category newCategory = new Category(categoryRequest.getName(), category);

      categoryService.addCategory(newCategory);

      return;
    }
    categoryService.addCategory(new Category(categoryRequest.getName()));
  }

  @GetMapping("/tree")
  public List<CategoryDTO> getCategories(
    @RequestParam(
      value = "treeLevel",
      defaultValue = Integer.MAX_VALUE + "",
      required = false
    ) Integer treeLevel,
    @RequestParam(
      value = "parentCategoryId",
      defaultValue = "-1",
      required = false
    ) Long parentCategoryId
  ) {
    if (parentCategoryId < 0) {
      return categoryService.getTreeCategoriesWithoutParent(treeLevel);
    }
    return categoryService.getTreeCategoriesWithParent(
      parentCategoryId,
      treeLevel
    );
  }

  @GetMapping
  public List<CategoryRequest> getAllCategories() {
    return categoryService
      .getAllCategories()
      .stream()
      .map(f ->
        new CategoryRequest(
          f.getId(),
          f.getName(),
          f.getCategory() != null ? f.getCategory().getId() : null
        )
      )
      .toList();
  }

  @DeleteMapping("/{id}/admin")
  public void removeCategory(@PathVariable("id") Long id) {
    categoryService.removeCategory(id);
  }

  @PutMapping("/{id}/admin")
  public void updateCategory(
    @PathVariable("id") Long id,
    @RequestBody CategoryRequest categoryRequest
  ) {
    if (categoryRequest.getParentCategoryId() != null) {
      Category category = categoryService.getCategoryById(
        categoryRequest.getParentCategoryId()
      );

      Category updateCategory = new Category(
        id,
        categoryRequest.getName(),
        category
      );

      categoryService.updateCategory(updateCategory);

      return;
    }
    categoryService.updateCategory(new Category(id, categoryRequest.getName()));
  }
}
