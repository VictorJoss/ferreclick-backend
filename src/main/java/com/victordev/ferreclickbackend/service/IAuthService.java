package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.DTOs.security.LoginRequest;
import com.victordev.ferreclickbackend.DTOs.security.LoginResponse;

/**
 * Interfaz del servicio de autenticación que proporciona métodos para iniciar y cerrar sesión, y comprobar si un usuario
 * es legal.
 */
public interface IAuthService {
    /**
     * Inicia sesión.
     * @param loginRequest Objeto que contiene las credenciales del usuario.
     * @return Objeto que contiene el token de acceso.
     */
    LoginResponse loginUser(LoginRequest loginRequest);

    /**
     * Cierra sesión.
     */
    void logout();

    /**
     * Comprueba si un usuario es legal.
     * @param userId Identificador del usuario.
     * @return true si el usuario es legal, false en caso contrario.
     */
    boolean isLegalUser(Long userId);
}
