package com.malak.expense_tracker.service.impl;

import com.malak.expense_tracker.dto.CategoryDTO;
import com.malak.expense_tracker.exception.CategoryAlreadyExistsException;
import com.malak.expense_tracker.exception.CategoryNotFoundException;
import com.malak.expense_tracker.mapper.CategoryMapper;
import com.malak.expense_tracker.model.Category;
import com.malak.expense_tracker.repository.CategoryRepository;
import com.malak.expense_tracker.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO dto) {
        if (categoryRepository.findByCategoryName(dto.categoryName()).isPresent()) {
            throw new CategoryAlreadyExistsException("Category already exists: " + dto.categoryName());
        }
        Category category = CategoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toDto(saved);
    }

    @Override
    public Optional<CategoryDTO> findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
                .map(CategoryMapper::toDto);
    }

    @Override
    public boolean existsByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).isPresent();
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO dto) {
        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));

        existing.setCategoryName(dto.categoryName());

        Category saved = categoryRepository.save(existing);
        return CategoryMapper.toDto(saved);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category not found with ID: " + categoryId);
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}