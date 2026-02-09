package com.malak.expense_tracker.mapper;

import com.malak.expense_tracker.dto.CategoryDTO;
import com.malak.expense_tracker.model.Category;

public class CategoryMapper {

    public static CategoryDTO toDto(Category category) {
        return new CategoryDTO(category.getCategoryId(), category.getCategoryName());
    }

    public static Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setCategoryId(dto.categoryId());
        category.setCategoryName(dto.categoryName());
        return category;
    }


}
