package com.victordev.ferreclickbackend.exceptions.product;

/**
 * Excepción lanzada cuando ocurre un error al crear un producto.
 */
public class ProductCreationException extends RuntimeException{

    /**
     * Constructor de la excepción.
     * @param message Mensaje de error.
     */
    public ProductCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}