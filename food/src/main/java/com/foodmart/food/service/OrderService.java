package com.foodmart.food.service;

import com.foodmart.food.dto.OrderRequest;
import com.foodmart.food.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(String username, OrderRequest request);
    List<OrderResponse> getOrders(String username);
}
