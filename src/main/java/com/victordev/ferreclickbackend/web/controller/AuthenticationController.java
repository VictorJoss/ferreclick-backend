package com.victordev.ferreclickbackend.web.controller;


import com.victordev.ferreclickbackend.DTOs.security.LoginRequest;
import com.victordev.ferreclickbackend.DTOs.security.LoginResponse;
import com.victordev.ferreclickbackend.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que maneja las peticiones relacionadas con la autenticación de usuarios.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    /**
     * Servicio de autenticación de usuarios.
     */
    private final IAuthService authService;

    /**
     * Autentica un usuario.
     * @param loginRequest Objeto que contiene las credenciales del usuario.
     * @return Respuesta HTTP que contiene el token de autenticación.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
            return ResponseEntity.ok(authService.loginUser(loginRequest));
    }

    /**
     * Cierra la sesión de un usuario.
     *
     */
    @PostMapping("/logout")
    public void logout(){
        authService.logout();
    }
}