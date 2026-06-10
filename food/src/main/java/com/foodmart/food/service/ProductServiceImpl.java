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
import java.util.Objects;

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
        Product product = Objects.requireNonNull(productMapper.toEntity(productDTO), "Product mapping produced null");

        Long categoryId = productDTO.getCategoryId();
        if (categoryId != null) {
            var cat = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
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
        List<ProductResponse> mapped = Objects.requireNonNull(page.getContent().stream().map(productMapper::toResponse).toList(), "Mapped product list must not be null");
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
        Long productId = Objects.requireNonNull(id, "Product id is required");
        logger.info("Updating product ID: {}", productId);
        Product existingProduct = Objects.requireNonNull(productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId)), "Existing product must not be null");

        productMapper.updateEntity(productDTO, existingProduct);

        Long categoryId = productDTO.getCategoryId();
        if (categoryId != null) {
            var cat = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
            existingProduct.setCategory(cat);
        }

        Product updated = Objects.requireNonNull(productRepository.save(existingProduct), "Updated product must not be null");
        logger.info("Product updated successfully");
        return updated;
    }

    @Override
    @Transactional
    public Product deleteProduct(Long id) {
        Long productId = Objects.requireNonNull(id, "Product id is required");
        logger.info("Deleting product ID: {}", productId);
        Product product = Objects.requireNonNull(productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId)), "Product to delete must not be null");

        productRepository.delete(product);
        logger.info("Product deleted successfully");
        return product;
    }

    @Override
    @Transactional
    public ProductResponse addImageToProduct(Long productId, org.springframework.web.multipart.MultipartFile file) throws java.io.IOException {
        Long safeProductId = Objects.requireNonNull(productId, "Product id is required");
        logger.info("Uploading image for product ID: {}", safeProductId);
        Product product = Objects.requireNonNull(productRepository.findById(safeProductId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + safeProductId)), "Product must not be null");
        
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

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Long productId = Objects.requireNonNull(id, "Product id is required");
        logger.info("Fetching product ID: {}", productId);
        Product product = Objects.requireNonNull(productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId)), "Product must not be null");
        return productMapper.toResponse(product);
    }
}
