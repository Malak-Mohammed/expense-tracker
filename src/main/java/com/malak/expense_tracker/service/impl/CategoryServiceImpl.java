package com.malak.expense_tracker.service.impl;

import com.malak.expense_tracker.dto.CategoryDTO;
import com.malak.expense_tracker.exception.CategoryAlreadyExistsException;
import com.malak.expense_tracker.exception.CategoryNotFoundException;
import com.malak.expense_tracker.mapper.CategoryMapper;
import com.malak.expense_tracker.model.Category;
import com.malak.expense_tracker.model.User;
import com.malak.expense_tracker.repository.CategoryRepository;
import com.malak.expense_tracker.repository.UserRepository;
import com.malak.expense_tracker.service.CategoryService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO dto) {
        User owner = userRepository.findByUsername(dto.ownerUsername())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        if (categoryRepository.existsByCategoryNameAndOwner(dto.categoryName(), owner)) {
            throw new CategoryAlreadyExistsException("Category already exists for user: " + dto.categoryName());
        }

        Category category = CategoryMapper.toEntity(dto, owner);
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
        return categoryRepository.existsByCategoryName(categoryName);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO dto) {
        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));

        User owner = userRepository.findByUsername(dto.ownerUsername())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        // Ownership check: only owner or ADMIN can update
        if (!existing.getOwner().equals(owner) && !owner.hasRole("ADMIN")) {
            throw new AccessDeniedException("You cannot update this category");
        }

        // Prevent duplicate category names for the same owner
        if (categoryRepository.existsByCategoryNameAndOwner(dto.categoryName(), owner)
                && !existing.getCategoryName().equalsIgnoreCase(dto.categoryName())) {
            throw new CategoryAlreadyExistsException("Category already exists: " + dto.categoryName());
        }

        existing.setCategoryName(dto.categoryName());
        Category saved = categoryRepository.save(existing);
        return CategoryMapper.toDto(saved);
    }

    @Override
    public void deleteCategory(Long categoryId, String username) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.hasRole("ADMIN") && !category.getOwner().equals(user)) {
            throw new AccessDeniedException("You cannot delete this category");
        }

        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.hasRole("ADMIN")) {
            return categoryRepository.findAll()
                    .stream()
                    .map(CategoryMapper::toDto)
                    .collect(Collectors.toList());
        }

        return categoryRepository.findByOwner(user)
                .stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}