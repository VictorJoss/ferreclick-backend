package com.victordev.ferreclickbackend.exceptions;

import com.victordev.ferreclickbackend.exceptions.Analytics.*;
import com.victordev.ferreclickbackend.exceptions.cart.CartNotFoundException;
import com.victordev.ferreclickbackend.exceptions.cart.PaymentProcessingException;
import com.victordev.ferreclickbackend.exceptions.cloudinary.CloudinaryException;
import com.victordev.ferreclickbackend.exceptions.files.*;
import com.victordev.ferreclickbackend.exceptions.product.*;
import com.victordev.ferreclickbackend.exceptions.productCategories.*;
import com.victordev.ferreclickbackend.exceptions.role.*;
import com.victordev.ferreclickbackend.exceptions.security.*;
import com.victordev.ferreclickbackend.DTOs.exception.support.ErrorResponse;
import com.victordev.ferreclickbackend.exceptions.user.UserAlreadyExistsException;
import com.victordev.ferreclickbackend.exceptions.user.UserNotFoundException;
import com.victordev.ferreclickbackend.exceptions.user.UserRegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manejador de excepciones globales.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Metodo reutilizable para construir una respuesta de error.
     *
     * @param status
     * @param message
     * @return
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(status.value(), message);
        return new ResponseEntity<>(error, status);
    }

// PRODUCT EXCEPTIONS

    /**
     * Maneja la excepción de creación de producto.
     *
     * @param ex Excepción de creación de producto.
     * @return Respuesta de error.
     */
    @ExceptionHandler(FailedProductCreationException.class)
    public ResponseEntity<ErrorResponse> handleProductCreationException(FailedProductCreationException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyExists(ProductAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotExist(ProductNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleProductException(ProductException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

// CATEGORY EXCEPTIONS

    /**
     * Maneja la excepción de categoría no encontrada.
     *
     * @param ex Excepción de categoría no encontrada.
     * @return Respuesta de error.
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFound(CategoryNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CategoryAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleCategoryAlreadyExists(CategoryAlreadyExistException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CategoryProductException.class)
    public ResponseEntity<ErrorResponse> handleCategoryProductException(CategoryProductException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

// CART EXCEPTIONS

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<ErrorResponse> handlePaymentProcessingException(PaymentProcessingException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFound(CartNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

// USER EXCEPTIONS

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<ErrorResponse> handleUserRegistrationException(UserRegistrationException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

// ROLE EXCEPTIONS

    @ExceptionHandler(TheRoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTheRoleNotFoundException(TheRoleNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

// ANALYTICS EXCEPTIONS

    @ExceptionHandler(AnalyticsDatabaseException.class)
    public ResponseEntity<ErrorResponse> handleAnalyticsDatabaseException(AnalyticsDatabaseException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(AnalyticsDataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAnalyticsDataNotFound(AnalyticsDataNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AnalyticsException.class)
    public ResponseEntity<ErrorResponse> handleAnalyticsUnexpectedException(AnalyticsException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

// FILES EXCEPTIONS

    @ExceptionHandler(InvalidImageException.class)
    public ResponseEntity<ErrorResponse> handleInvalidImage(InvalidImageException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ErrorResponse> handleImageUploadException(ImageUploadException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(DeleteImageException.class)
    public ResponseEntity<ErrorResponse> handleDeleteImageException(DeleteImageException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

// CLOUDINARY EXCEPTIONS

    @ExceptionHandler(CloudinaryException.class)
    public ResponseEntity<ErrorResponse> handleCloudinaryException(CloudinaryException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

// SECURITY EXCEPTIONS

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(IllegalUserException.class)
    public ResponseEntity<ErrorResponse> handleIllegalUser(IllegalUserException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(AuthUserException.class)
    public ResponseEntity<ErrorResponse> handleAuthUserException(AuthUserException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

// VALIDATION EXCEPTIONS

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, String.join(", ", errors));
    }

// GENERIC EXCEPTIONS

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }
}