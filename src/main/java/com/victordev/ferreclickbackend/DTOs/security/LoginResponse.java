package com.victordev.ferreclickbackend.DTOs.security;

/**
 * Objeto que contiene la información de la respuesta de inicio de sesión.
 */
public record LoginResponse (

        /**
         * Token de autenticación.
         */
        String jwt
)
{}
