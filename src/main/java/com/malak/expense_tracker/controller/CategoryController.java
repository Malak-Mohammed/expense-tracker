package com.malak.expense_tracker.controller;

import com.malak.expense_tracker.dto.CategoryDTO;
import com.malak.expense_tracker.response.ApiResponse;
import com.malak.expense_tracker.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> addCategory(@RequestBody CategoryDTO dto, Authentication auth) {
        // Inject logged-in user as owner
        CategoryDTO savedCategory = categoryService.addCategory(
                new CategoryDTO(dto.categoryId(), dto.categoryName(), auth.getName())
        );
        return buildResponse(true, "Category added successfully", savedCategory, HttpStatus.CREATED);
    }

    @GetMapping("/name/{categoryName}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryByName(@PathVariable String categoryName) {
        return categoryService.findByCategoryName(categoryName)
                .map(category -> buildResponse(true, "Category found", category, HttpStatus.OK))
                .orElse(buildResponse(false, "Category not found", null, HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable Long categoryId,
                                                                   @RequestBody CategoryDTO dto,
                                                                   Authentication auth) {
        CategoryDTO updatedCategory = categoryService.updateCategory(
                categoryId,
                new CategoryDTO(dto.categoryId(), dto.categoryName(), auth.getName())
        );
        return buildResponse(true, "Category updated successfully", updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long categoryId, Authentication auth) {
        // Pass logged-in user to service for ownership check
        categoryService.deleteCategory(categoryId, auth.getName());
        return buildResponse(true, "Category deleted successfully", null, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories(Authentication auth) {
        List<CategoryDTO> categories = categoryService.getAllCategories(auth.getName());
        return buildResponse(true, "Categories retrieved successfully", categories, HttpStatus.OK);
    }

    // --- Helper method to reduce repetition ---
    private <T> ResponseEntity<ApiResponse<T>> buildResponse(boolean success, String message, T data, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<>(success, message, data, status.value()));
    }
}