package com.foodmart.food.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {
    @NotNull(message = "Shipping address is required")
    @Valid
    private AddressDTO shippingAddress;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemDTO> items;

    public AddressDTO getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(AddressDTO shippingAddress) { this.shippingAddress = shippingAddress; }
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}
