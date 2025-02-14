package com.victordev.ferreclickbackend.exceptions.product;

/**
 * Excepción lanzada cuando ocurre un error al crear un producto.
 */
public class FailedProductCreationException extends RuntimeException{

    /**
     * Constructor de la excepción.
     * @param message Mensaje de error.
     */
    public FailedProductCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}