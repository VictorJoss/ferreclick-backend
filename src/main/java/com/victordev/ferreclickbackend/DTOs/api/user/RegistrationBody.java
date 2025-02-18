package com.victordev.ferreclickbackend.DTOs.api.user;

import jakarta.validation.constraints.*;
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
    @NotNull
    @NotBlank
    private String name;
    /**
     * Nombre de usuario.
     */
    @NotNull
    @NotBlank
    @Size(min=3, max=255)
    private String username;
    /**
     * Correo electrónico del usuario.
     */
    @NotNull
    @Email
    private String email;
    /**
     * Contraseña del usuario.
     */
    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")
    @Size(min=6, max=32)
    private String password;
    /**
     * Rol del usuario.
     */
    @NotNull
    @NotBlank
    private String role;
}
