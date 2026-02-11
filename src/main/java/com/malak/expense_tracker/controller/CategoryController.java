package com.malak.expense_tracker.controller;

import com.malak.expense_tracker.dto.CategoryDTO;
import com.malak.expense_tracker.response.ApiResponse;
import com.malak.expense_tracker.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<CategoryDTO>> addCategory(@RequestBody CategoryDTO dto) {
        CategoryDTO savedCategory = categoryService.addCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Category added successfully", savedCategory, HttpStatus.CREATED.value()));
    }

    @GetMapping("/name/{categoryName}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryByName(@PathVariable String categoryName) {
        return categoryService.findByCategoryName(categoryName)
                .map(category -> ResponseEntity.ok(new ApiResponse<>(true, "Category found", category, HttpStatus.OK.value())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Category not found", null, HttpStatus.NOT_FOUND.value())));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(@PathVariable Long categoryId,
                                                                   @RequestBody CategoryDTO dto) {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category updated successfully", updatedCategory, HttpStatus.OK.value()));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category deleted successfully", null, HttpStatus.OK.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(new ApiResponse<>(true, "Categories retrieved successfully", categories, HttpStatus.OK.value()));
    }
}