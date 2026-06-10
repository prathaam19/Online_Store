package com.foodmart.food.service;

import com.foodmart.food.dto.CategoryDTO;
import com.foodmart.food.entity.Category;
import com.foodmart.food.mapper.CategoryMapper;
import com.foodmart.food.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public Category addCategory(CategoryDTO categoryDTO) {
        logger.info("Adding new category: {}", categoryDTO.getName());
        Category category = Objects.requireNonNull(categoryMapper.toEntity(categoryDTO), "Category mapping produced null");
        Category saved = categoryRepository.save(category);
        logger.info("Category added successfully with ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        logger.info("Fetching all categories");
        return categoryRepository.findAll();
    }
}
