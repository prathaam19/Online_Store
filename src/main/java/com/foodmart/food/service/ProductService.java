package com.foodmart.food.service;

import com.foodmart.food.dto.ProductDTO;
import com.foodmart.food.dto.ProductResponse;
import com.foodmart.food.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product addProduct(ProductDTO productDTO);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getProductsByCategory(Long categoryId);
    Product updateProduct(Long id, ProductDTO productDTO);
    Product deleteProduct(Long id );

    Page<ProductResponse> getProductsPaged(Pageable pageable);
    List<ProductResponse> searchByName(String q);
    ProductResponse addImageToProduct(Long productId, org.springframework.web.multipart.MultipartFile file) throws java.io.IOException;
}
