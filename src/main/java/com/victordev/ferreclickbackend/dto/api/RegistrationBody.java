package com.victordev.ferreclickbackend.dto.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Objeto que contiene la información necesaria para registrar un usuario.
 */
@Getter
@Setter
public class RegistrationBody {

    /**
     * Nombre del usuario.
     */
    @NotBlank private String name;
    /**
     * Nombre de usuario.
     */
    @NotBlank private String username;
    /**
     * Correo electrónico del usuario.
     */
    @NotBlank private String email;
    /**
     * Contraseña del usuario.
     */
    @NotBlank private String password;
    /**
     * Rol del usuario.
     */
    @NotBlank private String role;
}
