package com.victordev.ferreclickbackend.dto.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Objeto que contiene la información de un usuario.
 */
@Getter
@Setter
public class UserDto {

    /**
     * Identificador del usuario.
     */
    private String name;
    /**
     * Nombre de usuario.
     */
    private String username;
    /**
     * Correo electrónico del usuario.
     */
    private String email;
    /**
     * Rol del usuario.
     */
    private String role;
}