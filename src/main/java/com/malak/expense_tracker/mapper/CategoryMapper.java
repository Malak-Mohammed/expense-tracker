package com.malak.expense_tracker.mapper;

import com.malak.expense_tracker.dto.CategoryDTO;
import com.malak.expense_tracker.model.Category;
import com.malak.expense_tracker.model.User;

public class CategoryMapper {

    public static CategoryDTO toDto(Category category) {
        return new CategoryDTO(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getOwner() != null ? category.getOwner().getUsername() : null
        );
    }

    public static Category toEntity(CategoryDTO dto, User owner) {
        Category category = new Category();
        category.setCategoryId(dto.categoryId());
        category.setCategoryName(dto.categoryName());
        category.setOwner(owner);
        return category;
    }
}