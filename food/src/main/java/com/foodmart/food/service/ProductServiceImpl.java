package com.foodmart.food.service;

import com.foodmart.food.dto.ProductDTO;
import com.foodmart.food.entity.Product;
import com.foodmart.food.exception.ResourceNotFoundException;
import com.foodmart.food.mapper.ProductMapper;
import com.foodmart.food.repository.ProductRepository;
import com.foodmart.food.repository.CategoryRepository;
import com.foodmart.food.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public Product addProduct(ProductDTO productDTO) {
        logger.info("Adding new product: {}", productDTO.getName());
        Product product = productMapper.toEntity(productDTO);

        if (productDTO.getCategoryId() != null) {
            var cat = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + productDTO.getCategoryId()));
            product.setCategory(cat);
        }

        Product saved = productRepository.save(product);
        logger.info("Product added successfully with ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        logger.info("Fetching products for category ID: {}", categoryId);
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream().map(productMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsPaged(Pageable pageable) {
        logger.info("Fetching products with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        var page = productRepository.findAll(pageable);
        List<ProductResponse> mapped = page.getContent().stream().map(productMapper::toResponse).toList();
        return new PageImpl<>(mapped, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchByName(String q) {
        logger.info("Searching products by name: {}", q);
        List<Product> products = productRepository.findByNameContainingIgnoreCase(q);
        return products.stream().map(productMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) {
        logger.info("Updating product ID: {}", id);
        Product existingProduct = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        productMapper.updateEntity(productDTO, existingProduct);

        if (productDTO.getCategoryId() != null) {
            var cat = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + productDTO.getCategoryId()));
            existingProduct.setCategory(cat);
        }

        Product updated = productRepository.save(existingProduct);
        logger.info("Product updated successfully");
        return updated;
    }

    @Override
    @Transactional
    public Product deleteProduct(Long id) {
        logger.info("Deleting product ID: {}", id);
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        productRepository.delete(product);
        logger.info("Product deleted successfully");
        return product;
    }

    @Override
    @Transactional
    public ProductResponse addImageToProduct(Long productId, org.springframework.web.multipart.MultipartFile file) throws java.io.IOException {
        logger.info("Uploading image for product ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        
        java.nio.file.Path uploads = java.nio.file.Paths.get("uploads").toAbsolutePath();
        java.nio.file.Files.createDirectories(uploads);
        String filename = System.currentTimeMillis() + "-" + java.util.UUID.randomUUID() + "-" + file.getOriginalFilename();
        java.nio.file.Path target = uploads.resolve(filename);
        try (var in = file.getInputStream()) {
            java.nio.file.Files.copy(in, target);
        }
        product.setImageFilename(filename);
        Product updated = productRepository.save(product);
        logger.info("Image uploaded successfully for product ID: {}", productId);
        return productMapper.toResponse(updated);
    }
}
