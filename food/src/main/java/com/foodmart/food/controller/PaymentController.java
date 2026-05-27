package com.foodmart.food.controller;

import com.foodmart.food.dto.PaymentOrderRequest;
import com.foodmart.food.dto.PaymentOrderResponse;
import com.foodmart.food.dto.PaymentVerifyRequest;
import com.foodmart.food.service.PaymentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public PaymentOrderResponse createPaymentOrder(@AuthenticationPrincipal String username,
                                                   @RequestBody PaymentOrderRequest request) {
        return paymentService.createPaymentOrder(username, request);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/verify")
    public String verifyPayment(@AuthenticationPrincipal String username,
                                @RequestBody PaymentVerifyRequest request) {
        return paymentService.verifyPayment(username, request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<PaymentOrderResponse> getUserPayments(@AuthenticationPrincipal String username) {
        return paymentService.getUserPayments(username);
    }
}
