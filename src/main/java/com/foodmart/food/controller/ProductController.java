package com.foodmart.food.controller;

import com.foodmart.food.dto.ProductDTO;
import com.foodmart.food.dto.ProductResponse;
import com.foodmart.food.entity.Product;
import com.foodmart.food.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {

        this.productService = productService;
    }

    @PostMapping("/add")
    public Product addProduct(@Valid @RequestBody ProductDTO productDTO){
        return productService.addProduct(productDTO);
    }

    @GetMapping("/get")
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductResponse> getByCategory(@PathVariable Long categoryId){
        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping
    public Page<ProductResponse> listPaged(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Pageable p = PageRequest.of(page, size);
        return productService.getProductsPaged(p);
    }

    @GetMapping("/search")
    public List<ProductResponse> search(@RequestParam String q) {
        return productService.searchByName(q);
    }

    @PostMapping(path = "/{id}/image", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        var resp = productService.addImageToProduct(id, file);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO){
        return productService.updateProduct(id, productDTO);
    }

    @DeleteMapping("/{id}")
    public String DeleteProduct(@PathVariable Long id ){
        productService.deleteProduct(id);
        return "Product Deleted Successfully";
    }
}
