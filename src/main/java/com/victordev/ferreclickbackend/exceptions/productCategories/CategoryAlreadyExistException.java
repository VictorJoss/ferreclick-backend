package com.victordev.ferreclickbackend.exceptions.productCategories;

public class CategoryAlreadyExistException extends RuntimeException {
    public CategoryAlreadyExistException(String message) {
        super(message);
    }
}
