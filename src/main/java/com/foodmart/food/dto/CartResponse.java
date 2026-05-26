package com.foodmart.food.dto;

import java.util.List;

public class CartResponse {
    private Long id;
    private String username;
    private List<ProductResponse> products;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public List<ProductResponse> getProducts() { return products; }
    public void setProducts(List<ProductResponse> products) { this.products = products; }
}
