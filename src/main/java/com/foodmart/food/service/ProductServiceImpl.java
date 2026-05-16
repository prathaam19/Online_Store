package com.foodmart.food.service;

import com.foodmart.food.dto.ProductDTO;
import com.foodmart.food.entity.Product;
import com.foodmart.food.mapper.ProductMapper;
import com.foodmart.food.repository.ProductRepository;
import com.foodmart.food.repository.CategoryRepository;
import com.foodmart.food.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Product addProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);

        if (productDTO.getCategoryId() != null) {
            var cat = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(cat);
        }

        return productRepository.save(product);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toResponse).toList();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream().map(productMapper::toResponse).toList();
    }

    @Override
    public Page<ProductResponse> getProductsPaged(Pageable pageable) {
        var page = productRepository.findAll(pageable);
        List<ProductResponse> mapped = page.getContent().stream().map(productMapper::toResponse).toList();
        return new PageImpl<>(mapped, pageable, page.getTotalElements());
    }

    @Override
    public List<ProductResponse> searchByName(String q) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(q);
        return products.stream().map(productMapper::toResponse).toList();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository
                .findById(id).
                orElseThrow(() -> new RuntimeException("product not found"));

        productMapper.updateEntity(productDTO, existingProduct);

        if (productDTO.getCategoryId() != null) {
            var cat = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingProduct.setCategory(cat);
        }

        return productRepository.save(existingProduct);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Product deleteProduct(Long id) {
        Product product = productRepository
                .findById(id).
                orElseThrow(() -> new RuntimeException("product not found"));

            productRepository.delete(product);
            return product;
    }

    @Override
    public ProductResponse addImageToProduct(Long productId, org.springframework.web.multipart.MultipartFile file) throws java.io.IOException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("product not found"));
        java.nio.file.Path uploads = java.nio.file.Paths.get("uploads").toAbsolutePath();
        java.nio.file.Files.createDirectories(uploads);
        String filename = System.currentTimeMillis() + "-" + java.util.UUID.randomUUID() + "-" + file.getOriginalFilename();
        java.nio.file.Path target = uploads.resolve(filename);
        try (var in = file.getInputStream()) {
            java.nio.file.Files.copy(in, target);
        }
        product.setImageFilename(filename);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }
}
