package com.foodmart.food.dto;

import java.util.List;

public class OrderRequest {
    private AddressDTO shippingAddress;
    private List<OrderItemDTO> items;

    public AddressDTO getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(AddressDTO shippingAddress) { this.shippingAddress = shippingAddress; }
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
}
