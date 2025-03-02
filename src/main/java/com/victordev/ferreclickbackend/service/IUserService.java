package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.DTOs.api.user.RegistrationBody;
import com.victordev.ferreclickbackend.DTOs.api.user.UserDto;
import com.victordev.ferreclickbackend.exceptions.user.UserAlreadyExistsException;

/**
 * Interfaz del servicio de usuario que proporciona métodos para registrar un usuario, iniciar sesión, cerrar sesión y obtener un usuario.
 */
public interface IUserService {

    /**
     * Registra un usuario.
     *
     * @param registrationBody Objeto que contiene la información del usuario a registrar.
     * @throws UserAlreadyExistsException Si el usuario ya existe.
     */
    void registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException;

    /**
     * Obtiene un usuario.
     * @param userId Identificador del usuario.
     * @return Objeto que contiene la información del usuario.
     */
    UserDto getUser(Long userId);
}
