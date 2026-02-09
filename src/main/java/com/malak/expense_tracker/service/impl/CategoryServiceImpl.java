package com.malak.expense_tracker.service.impl;

import com.malak.expense_tracker.model.Category;
import com.malak.expense_tracker.repository.CategoryRepository;
import com.malak.expense_tracker.service.CategoryService;

import java.util.List;
import java.util.Optional;

public class CategoryServiceImpl implements CategoryService {

   private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Category addCategory(Category category) {
        if (categoryRepository.findByCategoryName(category.getCategoryName()).isPresent()) {
            throw new IllegalArgumentException("Category already exists");
        }
        return categoryRepository.save(category);

    }

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);

    }

    @Override
    public boolean existsByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).isPresent();
    }

    @Override
    public Category updateCategory(Long categoryId, Category updatedCategory) {
        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        existing.setCategoryName(updatedCategory.getCategoryName());


        return categoryRepository.save(existing);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("Category not found");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
