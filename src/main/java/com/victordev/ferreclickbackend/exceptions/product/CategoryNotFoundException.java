package com.victordev.ferreclickbackend.exceptions.product;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}