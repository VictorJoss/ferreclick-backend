package com.victordev.ferreclickbackend.exceptions.handler;

import com.victordev.ferreclickbackend.exceptions.product.CategoryNotFoundException;
import com.victordev.ferreclickbackend.exceptions.product.ProductCreationException;
import com.victordev.ferreclickbackend.exceptions.support.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador de excepciones globales.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja la excepción de categoría no encontrada.
     * @param ex Excepción de categoría no encontrada.
     * @return Respuesta de error.
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFound(CategoryNotFoundException ex){
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja la excepción de creación de producto.
     * @param ex Excepción de creación de producto.
     * @return Respuesta de error.
     */
    @ExceptionHandler(ProductCreationException.class)
    public ResponseEntity<ErrorResponse> handleProductCreationException(ProductCreationException ex){
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}