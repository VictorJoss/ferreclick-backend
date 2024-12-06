package com.victordev.ferreclickbackend.service;

import com.victordev.ferreclickbackend.dto.api.RegistrationBody;
import com.victordev.ferreclickbackend.dto.api.UserDto;
import com.victordev.ferreclickbackend.dto.security.LoginRequest;
import com.victordev.ferreclickbackend.dto.security.LoginResponse;
import com.victordev.ferreclickbackend.exceptions.user.UserAlreadyExistsException;
import com.victordev.ferreclickbackend.persistence.entity.User;

/**
 * Interfaz del servicio de usuario que proporciona métodos para registrar un usuario, iniciar sesión, cerrar sesión y obtener un usuario.
 */
public interface IUserService {

    /**
     * Registra un usuario.
     * @param registrationBody Objeto que contiene la información del usuario a registrar.
     * @return Objeto que contiene la información del usuario registrado.
     * @throws UserAlreadyExistsException Si el usuario ya existe.
     */
    User registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException;

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
     * Obtiene un usuario.
     * @param userId Identificador del usuario.
     * @return Objeto que contiene la información del usuario.
     */
    UserDto getUser(Long userId);
}
