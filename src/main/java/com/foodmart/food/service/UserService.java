package com.foodmart.food.service;

import com.foodmart.food.dto.AuthResponse;
import com.foodmart.food.dto.RegisterRequest;

public interface UserService {
    AuthResponse register(RegisterRequest request);
}
