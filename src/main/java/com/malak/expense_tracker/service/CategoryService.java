package com.malak.expense_tracker.service;

import com.malak.expense_tracker.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    CategoryDTO addCategory(CategoryDTO categoryDTO);

    Optional<CategoryDTO> findByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO updatedCategoryDTO);

    void deleteCategory(Long categoryId);

    List<CategoryDTO> getAllCategories();
}