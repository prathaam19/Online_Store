package com.foodmart.food.repository;

import com.foodmart.food.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);
    List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Payment> findByOrderId(Long orderId);
}
