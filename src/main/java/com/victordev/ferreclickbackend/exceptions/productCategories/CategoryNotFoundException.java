package com.victordev.ferreclickbackend.exceptions.productCategories;

/**
 * Excepción lanzada cuando no se encuentra una categoría.
 */
public class CategoryNotFoundException extends RuntimeException {

    /**
     * Constructor de la excepción.
     * @param message Mensaje de error.
     */
    public CategoryNotFoundException(String message) {
        super(message);
    }
}