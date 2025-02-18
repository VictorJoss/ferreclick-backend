package com.victordev.ferreclickbackend.DTOs.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Objeto que contiene la información de la solicitud de inicio de sesión.
 */
public record LoginRequest(

        /**
         * Correo electrónico del usuario.
         */
        @NotNull
        @NotBlank
        @Email
        String email,
        /**
         * Contraseña del usuario.
         */
        @NotNull
        @NotBlank
        String password
)
{}
