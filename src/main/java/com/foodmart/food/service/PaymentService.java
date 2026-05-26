package com.foodmart.food.service;

import com.foodmart.food.dto.PaymentOrderRequest;
import com.foodmart.food.dto.PaymentOrderResponse;
import com.foodmart.food.dto.PaymentVerifyRequest;

import java.util.List;

public interface PaymentService {
    PaymentOrderResponse createPaymentOrder(String username, PaymentOrderRequest request);
    String verifyPayment(String username, PaymentVerifyRequest request);
    List<PaymentOrderResponse> getUserPayments(String username);
}
