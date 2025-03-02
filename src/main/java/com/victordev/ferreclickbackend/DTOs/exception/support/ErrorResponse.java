package com.victordev.ferreclickbackend.DTOs.exception.support;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Objeto que contiene la información de un error.
 */
@Getter
@Setter
public class ErrorResponse {

    /**
     * Código de estado del error.
     */
    private int status;
    /**
     * Mensaje del error.
     */
    private String message;
    /**
     * Fecha y hora en la que ocurrió el error.
     */
    private LocalDateTime timestamp;

    /**
     * Constructor de la clase.
     * @param status Código de estado del error.
     * @param message Mensaje del error.
     */
    public ErrorResponse(int status, String message){
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
