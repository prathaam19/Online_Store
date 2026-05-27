package com.foodmart.food.controller;

import com.foodmart.food.dto.CategoryDTO;
import com.foodmart.food.entity.Category;
import com.foodmart.food.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService=categoryService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Category addCategory(@RequestBody CategoryDTO categoryDTO) {
            return  categoryService.addCategory(categoryDTO);
    }

    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }
}
