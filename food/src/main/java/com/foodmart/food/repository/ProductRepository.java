package com.foodmart.food.repository;

import com.foodmart.food.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	java.util.List<Product> findByCategoryId(Long categoryId);
	java.util.List<Product> findByNameContainingIgnoreCase(String name);
}
