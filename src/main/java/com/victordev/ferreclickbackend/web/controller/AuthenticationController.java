package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.DTOs.api.user.RegistrationBody;
import com.victordev.ferreclickbackend.DTOs.security.LoginRequest;
import com.victordev.ferreclickbackend.DTOs.security.LoginResponse;
import com.victordev.ferreclickbackend.exceptions.user.UserAlreadyExistsException;
import com.victordev.ferreclickbackend.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que maneja las peticiones relacionadas con la autenticación de usuarios.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    /**
     * Servicio de usuario.
     */
    @Autowired
    private IUserService userService;

    /**
     * Registra un nuevo usuario.
     * @param registrationBody Objeto que contiene la información del usuario a registrar.
     * @return Respuesta HTTP que indica si el usuario ha sido registrado correctamente.
     */
    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody @Valid RegistrationBody registrationBody) {

        try{
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        }catch (UserAlreadyExistsException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Autentica un usuario.
     * @param loginRequest Objeto que contiene las credenciales del usuario.
     * @return Respuesta HTTP que contiene el token de autenticación.
     */
    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(userService.loginUser(loginRequest));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Cierra la sesión de un usuario.
     * @throws Exception
     */
    @PreAuthorize("permitAll()")
    @PostMapping("/logout")
    public void logout() throws Exception{
        userService.logout();
    }
}