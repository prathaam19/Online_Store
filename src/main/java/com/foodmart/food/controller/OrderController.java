package com.foodmart.food.controller;

import com.foodmart.food.dto.OrderRequest;
import com.foodmart.food.dto.OrderResponse;
import com.foodmart.food.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse placeOrder(@AuthenticationPrincipal String username, @RequestBody OrderRequest request) {
        return orderService.placeOrder(username, request);
    }

    @GetMapping
    public List<OrderResponse> getOrders(@AuthenticationPrincipal String username) {
        return orderService.getOrders(username);
    }
}
