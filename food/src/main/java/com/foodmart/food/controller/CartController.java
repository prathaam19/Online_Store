package com.foodmart.food.controller;

import com.foodmart.food.dto.CartResponse;
import com.foodmart.food.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public CartResponse getCart(@AuthenticationPrincipal String username) {
        return cartService.getCart(username);
    }

    @PostMapping("/add/{productId}")
    public CartResponse addProduct(@AuthenticationPrincipal String username, @PathVariable Long productId) {
        return cartService.addProductToCart(username, productId);
    }

    @DeleteMapping("/remove/{productId}")
    public CartResponse removeProduct(@AuthenticationPrincipal String username, @PathVariable Long productId) {
        return cartService.removeProductFromCart(username, productId);
    }
}
