package com.malak.expense_tracker.service;

import com.malak.expense_tracker.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category addCategory(Category category);
    Optional<Category> findByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);

    Category updateCategory(Long categoryId, Category updatedCategory);
    void deleteCategory(Long categoryId);
    List<Category> getAllCategories();

}
