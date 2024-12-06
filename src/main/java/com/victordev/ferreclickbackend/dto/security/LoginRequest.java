package com.victordev.ferreclickbackend.dto.security;

import jakarta.validation.constraints.NotBlank;

/**
 * Objeto que contiene la información de la solicitud de inicio de sesión.
 */
public record LoginRequest(

        /**
         * Correo electrónico del usuario.
         */
        @NotBlank String email,
        /**
         * Contraseña del usuario.
         */
        @NotBlank String password
)
{}
