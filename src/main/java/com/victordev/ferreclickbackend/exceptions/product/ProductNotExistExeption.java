package com.victordev.ferreclickbackend.exceptions.product;

public class ProductNotExistExeption extends RuntimeException {
    public ProductNotExistExeption(String message) {
        super(message);
    }
}
