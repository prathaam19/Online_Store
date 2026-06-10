package com.foodmart.food.service;

import com.foodmart.food.dto.PaymentOrderRequest;
import com.foodmart.food.dto.PaymentOrderResponse;
import com.foodmart.food.dto.PaymentVerifyRequest;
import com.foodmart.food.entity.Order;
import com.foodmart.food.entity.Payment;
import com.foodmart.food.entity.PaymentStatus;
import com.foodmart.food.entity.User;
import com.foodmart.food.exception.ResourceNotFoundException;
import com.foodmart.food.repository.OrderRepository;
import com.foodmart.food.repository.PaymentRepository;
import com.foodmart.food.repository.UserRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RazorpayClient razorpayClient;
    private final String razorpayKeySecret;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderRepository orderRepository,
                              UserRepository userRepository,
                              ObjectProvider<EmailService> emailServiceProvider,
                              Environment environment) throws RazorpayException {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.emailService = emailServiceProvider.getIfAvailable();

        String razorpayKeyId = environment.getProperty("razorpay.key-id");
        if (razorpayKeyId == null) {
            razorpayKeyId = environment.getProperty("razorpay.api.key");
        }

        String razorpayKeySecret = environment.getProperty("razorpay.key-secret");
        if (razorpayKeySecret == null) {
            razorpayKeySecret = environment.getProperty("razorpay.api.secret");
        }

        // Demo fallback credentials when explicit configuration is missing
        if (razorpayKeyId == null || razorpayKeyId.isBlank()) {
            razorpayKeyId = "rzp_test_BLtQ4MAIVBksGL";
        }
        if (razorpayKeySecret == null || razorpayKeySecret.isBlank()) {
            razorpayKeySecret = "OsuNHnliz3IYLbOteAJVOzrq";
        }

        this.razorpayKeySecret = razorpayKeySecret;
        this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
    }

    @Override
    public PaymentOrderResponse createPaymentOrder(String username, PaymentOrderRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Long orderId = Objects.requireNonNull(request.getOrderId(), "Order id is required");
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getUsername().equals(username)) {
            throw new ResourceNotFoundException("Order does not belong to authenticated user");
        }

        BigDecimal amount = order.getTotal();
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount.multiply(BigDecimal.valueOf(100)).longValue());
        orderRequest.put("currency", request.getCurrency() == null ? "INR" : request.getCurrency());
        orderRequest.put("receipt", "order_rcptid_" + order.getId());
        orderRequest.put("payment_capture", 1);

        try {
            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            String razorpayOrderId = razorpayOrder.get("id").toString();

            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setUser(user);
            payment.setAmount(amount);
            payment.setCurrency(request.getCurrency() == null ? "INR" : request.getCurrency());
            payment.setDescription(request.getDescription());
            payment.setRazorpayOrderId(razorpayOrderId);
            payment.setStatus(PaymentStatus.PENDING);
            payment.setCreatedAt(LocalDateTime.now());
            payment.setUpdatedAt(LocalDateTime.now());
            payment.setMethod("razorpay");

            Payment savedPayment = paymentRepository.save(payment);
            logger.info("Created Razorpay order {} for payment {}", razorpayOrderId, savedPayment.getId());

            return new PaymentOrderResponse(savedPayment.getId(), razorpayOrderId, savedPayment.getAmount(), savedPayment.getCurrency(), savedPayment.getStatus().name(), savedPayment.getDescription());
        } catch (RazorpayException e) {
            logger.error("Razorpay order creation failed", e);
            throw new RuntimeException("Unable to create Razorpay order: " + e.getMessage());
        }
    }

    @Override
    public String verifyPayment(String username, PaymentVerifyRequest request) {
        Payment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found"));

        if (!payment.getUser().getUsername().equals(username)) {
            throw new ResourceNotFoundException("Payment does not belong to authenticated user");
        }

        boolean valid = verifyRazorpaySignature(request.getRazorpayOrderId(), request.getRazorpayPaymentId(), request.getRazorpaySignature());
        if (!valid) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
            if (emailService != null) {
                emailService.sendPaymentFailedEmail(payment.getUser().getEmail(), payment.getUser().getUsername(), payment.getOrder().getId(), "Signature verification failed");
            }
            throw new RuntimeException("Invalid Razorpay payment signature");
        }

        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setRazorpaySignature(request.getRazorpaySignature());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        if (emailService != null) {
            emailService.sendPaymentSuccessEmail(payment.getUser().getEmail(), payment.getUser().getUsername(), payment.getOrder().getId(), payment.getAmount().toString(), request.getRazorpayPaymentId());
        }

        return "Payment verified successfully";
    }

    @Override
    public List<PaymentOrderResponse> getUserPayments(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Payment> payments = paymentRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<PaymentOrderResponse> response = new ArrayList<>();
        for (Payment payment : payments) {
            response.add(new PaymentOrderResponse(payment.getId(), payment.getRazorpayOrderId(), payment.getAmount(), payment.getCurrency(), payment.getStatus().name(), payment.getDescription()));
        }
        return response;
    }

    private boolean verifyRazorpaySignature(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(razorpayKeySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] expected = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(expected).equalsIgnoreCase(signature);
        } catch (Exception e) {
            logger.error("Failed to verify Razorpay signature", e);
            return false;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
