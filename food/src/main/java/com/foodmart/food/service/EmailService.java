package com.foodmart.food.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Service
@ConditionalOnProperty(prefix = "spring.mail", name = "host")
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${mail.from:noreply@foodmart.com}")
    private String mailFrom;

    @Value("${mail.from.name:FoodMart}")
    private String mailFromName;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send order confirmation email
     */
    public void sendOrderConfirmationEmail(String toEmail, String username, Long orderId, String orderAmount) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            applyMailFrom(helper);
            String recipient = Objects.requireNonNull(toEmail, "Email recipient must be provided");
            helper.setTo(recipient);
            helper.setSubject("Order Confirmation - FoodMart");

            String htmlContent = Objects.requireNonNull(buildOrderConfirmationEmail(username, orderId, orderAmount), "Email content must not be null");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Order confirmation email sent to: {}", toEmail);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Failed to send order confirmation email to: {}", toEmail, e);
        }
    }

    /**
     * Send payment success email
     */
    public void sendPaymentSuccessEmail(String toEmail, String username, Long orderId, String amount, String transactionId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            applyMailFrom(helper);
            String recipient = Objects.requireNonNull(toEmail, "Email recipient must be provided");
            helper.setTo(recipient);
            helper.setSubject("Payment Successful - Order #" + orderId);

            String htmlContent = Objects.requireNonNull(buildPaymentSuccessEmail(username, orderId, amount, transactionId), "Email content must not be null");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Payment success email sent to: {}", toEmail);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Failed to send payment success email to: {}", toEmail, e);
        }
    }

    /**
     * Send payment failed email
     */
    public void sendPaymentFailedEmail(String toEmail, String username, Long orderId, String reason) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            applyMailFrom(helper);
            String recipient = Objects.requireNonNull(toEmail, "Email recipient must be provided");
            helper.setTo(recipient);
            helper.setSubject("Payment Failed - Order #" + orderId);

            String htmlContent = Objects.requireNonNull(buildPaymentFailedEmail(username, orderId, reason), "Email content must not be null");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Payment failed email sent to: {}", toEmail);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Failed to send payment failed email to: {}", toEmail, e);
        }
    }

    /**
     * Send order delivery notification
     */
    public void sendOrderDeliveredEmail(String toEmail, String username, Long orderId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            applyMailFrom(helper);
            String recipient = Objects.requireNonNull(toEmail, "Email recipient must be provided");
            helper.setTo(recipient);
            helper.setSubject("Your Order Has Been Delivered - FoodMart");

            String htmlContent = Objects.requireNonNull(buildOrderDeliveredEmail(username, orderId), "Email content must not be null");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Order delivered email sent to: {}", toEmail);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Failed to send order delivered email to: {}", toEmail, e);
        }
    }

    private void applyMailFrom(MimeMessageHelper helper) throws MessagingException, UnsupportedEncodingException {
        helper.setFrom(
                Objects.requireNonNull(mailFrom, "Mail from address must be configured"),
                Objects.requireNonNull(mailFromName, "Mail from name must be configured")
        );
    }

    // ===== Email Template Builders =====

    private String buildOrderConfirmationEmail(String username, Long orderId, String amount) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;'>" +
                "<div style='background-color: white; padding: 30px; border-radius: 10px; max-width: 600px; margin: auto;'>" +
                "<h2 style='color: #FF6B35;'>Order Confirmed! 🎉</h2>" +
                "<p>Hi <strong>" + username + "</strong>,</p>" +
                "<p>Thank you for your order! Your order has been confirmed and is being prepared.</p>" +
                "<div style='background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 20px 0;'>" +
                "<p><strong>Order Details:</strong></p>" +
                "<p><strong>Order ID:</strong> #" + orderId + "</p>" +
                "<p><strong>Amount:</strong> ₹" + amount + "</p>" +
                "<p><strong>Status:</strong> <span style='color: #4CAF50;'>Confirmed</span></p>" +
                "</div>" +
                "<p>You will receive another update once your order is on the way!</p>" +
                "<p>Thank you for choosing FoodMart! 😊</p>" +
                "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'>" +
                "<p style='color: #999; font-size: 12px;'>© 2026 FoodMart. All rights reserved.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String buildPaymentSuccessEmail(String username, Long orderId, String amount, String transactionId) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;'>" +
                "<div style='background-color: white; padding: 30px; border-radius: 10px; max-width: 600px; margin: auto;'>" +
                "<h2 style='color: #4CAF50;'>Payment Successful! ✅</h2>" +
                "<p>Hi <strong>" + username + "</strong>,</p>" +
                "<p>Your payment has been processed successfully.</p>" +
                "<div style='background-color: #f0f8f0; padding: 15px; border-radius: 5px; margin: 20px 0;'>" +
                "<p><strong>Payment Details:</strong></p>" +
                "<p><strong>Order ID:</strong> #" + orderId + "</p>" +
                "<p><strong>Amount Paid:</strong> ₹" + amount + "</p>" +
                "<p><strong>Transaction ID:</strong> " + transactionId + "</p>" +
                "<p><strong>Status:</strong> <span style='color: #4CAF50;'>Completed</span></p>" +
                "</div>" +
                "<p>Your food will be delivered soon. Track your order in the app!</p>" +
                "<p>Thank you for your order! 🙏</p>" +
                "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'>" +
                "<p style='color: #999; font-size: 12px;'>© 2026 FoodMart. All rights reserved.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String buildPaymentFailedEmail(String username, Long orderId, String reason) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;'>" +
                "<div style='background-color: white; padding: 30px; border-radius: 10px; max-width: 600px; margin: auto;'>" +
                "<h2 style='color: #FF5252;'>Payment Failed ❌</h2>" +
                "<p>Hi <strong>" + username + "</strong>,</p>" +
                "<p>Unfortunately, your payment could not be processed.</p>" +
                "<div style='background-color: #ffebee; padding: 15px; border-radius: 5px; margin: 20px 0;'>" +
                "<p><strong>Order ID:</strong> #" + orderId + "</p>" +
                "<p><strong>Reason:</strong> " + reason + "</p>" +
                "</div>" +
                "<p>Please try again or contact our support team if you need assistance.</p>" +
                "<p>We're here to help! 😊</p>" +
                "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'>" +
                "<p style='color: #999; font-size: 12px;'>© 2026 FoodMart. All rights reserved.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String buildOrderDeliveredEmail(String username, Long orderId) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;'>" +
                "<div style='background-color: white; padding: 30px; border-radius: 10px; max-width: 600px; margin: auto;'>" +
                "<h2 style='color: #4CAF50;'>Order Delivered! 🚀</h2>" +
                "<p>Hi <strong>" + username + "</strong>,</p>" +
                "<p>Great news! Your order has been successfully delivered!</p>" +
                "<div style='background-color: #f0f8f0; padding: 15px; border-radius: 5px; margin: 20px 0;'>" +
                "<p><strong>Order ID:</strong> #" + orderId + "</p>" +
                "<p><strong>Status:</strong> <span style='color: #4CAF50;'>Delivered</span></p>" +
                "</div>" +
                "<p>We hope you enjoyed your meal! If you have any feedback, we'd love to hear from you.</p>" +
                "<p>Thanks for ordering from FoodMart! 😊</p>" +
                "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'>" +
                "<p style='color: #999; font-size: 12px;'>© 2026 FoodMart. All rights reserved.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
