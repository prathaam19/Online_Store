package com.foodmart.food.dto;

import java.math.BigDecimal;

public class PaymentOrderRequest {
    private Long orderId;
    private BigDecimal amount;
    private String currency = "INR";
    private String description;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
