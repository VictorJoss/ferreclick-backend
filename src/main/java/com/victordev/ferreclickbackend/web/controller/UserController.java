package com.victordev.ferreclickbackend.web.controller;

import com.victordev.ferreclickbackend.DTOs.api.user.UserDto;
import com.victordev.ferreclickbackend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que maneja las peticiones relacionadas con los usuarios.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * Servicio de usuario.
     */
    @Autowired
    private IUserService userService;

    /**
     * Obtiene un usuario por su identificador.
     *
     * @param userId Identificador del usuario.
     * @return Respuesta HTTP que contiene la información del usuario.
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }
}
