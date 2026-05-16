package com.foodmart.food.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException( String message) {
        super(message);
    }
}
