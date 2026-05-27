package com.foodmart.food.service;

import com.foodmart.food.dto.CartResponse;

public interface CartService {
    CartResponse getCart(String username);
    CartResponse addProductToCart(String username, Long productId);
    CartResponse removeProductFromCart(String username, Long productId);
}
