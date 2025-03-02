package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.DTOs.api.user.RegistrationBody;
import com.victordev.ferreclickbackend.DTOs.api.user.UserDto;
import com.victordev.ferreclickbackend.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que maneja las peticiones relacionadas con los usuarios.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    /**
     * Servicio de usuario.
     */
    private final IUserService userService;

    /**
     * Registra un nuevo usuario.
     * @param registrationBody Objeto que contiene la información del usuario a registrar.
     * @return Respuesta HTTP que indica si el usuario ha sido registrado correctamente.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationBody registrationBody) {

        userService.registerUser(registrationBody);
        return ResponseEntity.status(201).build();
    }

    /**
     * Obtiene un usuario por su identificador.
     *
     * @param userId Identificador del usuario.
     * @return Respuesta HTTP que contiene la información del usuario.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }
}
