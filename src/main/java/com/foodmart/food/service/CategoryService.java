package com.foodmart.food.service;

import com.foodmart.food.dto.CategoryDTO;
import com.foodmart.food.entity.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(CategoryDTO categoryDTO);
    List<Category> getAllCategories();
}
