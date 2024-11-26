package com.victordev.ferreclickbackend.exceptions.product;

public class ProductCreationException extends RuntimeException{

    public ProductCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}