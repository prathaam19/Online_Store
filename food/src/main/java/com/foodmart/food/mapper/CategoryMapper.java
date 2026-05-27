package com.foodmart.food.mapper;

import com.foodmart.food.dto.CategoryDTO;
import com.foodmart.food.entity.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    private final ModelMapper modelMapper;

    public CategoryMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CategoryDTO toDto(Category category) {
        if (category == null) {
            return null;
        }
        return modelMapper.map(category, CategoryDTO.class);
    }

    public Category toEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }
        return modelMapper.map(categoryDTO, Category.class);
    }
}
