package com.foodmart.food.mapper;

import com.foodmart.food.dto.ProductDTO;
import com.foodmart.food.dto.ProductResponse;
import com.foodmart.food.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    private final ModelMapper modelMapper;
    private final CategoryMapper categoryMapper;

    public ProductMapper(ModelMapper modelMapper, CategoryMapper categoryMapper) {
        this.modelMapper = modelMapper;
        this.categoryMapper = categoryMapper;
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        return product;
    }

    public void updateEntity(ProductDTO dto, Product product) {
        if (dto == null || product == null) {
            return;
        }
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
    }

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }
        ProductResponse response = modelMapper.map(product, ProductResponse.class);
        response.setCategory(categoryMapper.toDto(product.getCategory()));
        return response;
    }
}
