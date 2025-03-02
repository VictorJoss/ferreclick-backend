package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.DTOs.api.user.RegistrationBody;
import com.victordev.ferreclickbackend.DTOs.api.user.UserDto;
import com.victordev.ferreclickbackend.exceptions.user.UserAlreadyExistsException;
import com.victordev.ferreclickbackend.exceptions.user.UserRegistrationException;
import com.victordev.ferreclickbackend.persistence.entity.Cart;
import com.victordev.ferreclickbackend.persistence.entity.Role;
import com.victordev.ferreclickbackend.persistence.entity.User;
import com.victordev.ferreclickbackend.persistence.repository.UserRepository;
import com.victordev.ferreclickbackend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Implementación del servicio de usuarios que proporciona métodos para registrar y autenticar usuarios.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    /**
     * Repositorio de usuarios.
     */
    private final UserRepository userRepository;
    /**
     * Servicio de búsqueda de entidades.
     */
    private final IEntityFinderService entityFinderService;
    /**
     * Codificador de contraseñas.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * Servicio de carrito.
     */
    private final ICartService cartService;

    /**
     * Registra un nuevo usuario.
     *
     * @param registrationBody Objeto que contiene la información del usuario a registrar.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {

        boolean userExists = userRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()
                || userRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent();

        if (userExists) {
            throw new UserAlreadyExistsException("User already exists with the provided username or email");
        }

        try {
            String encodedPassword = passwordEncoder.encode(registrationBody.getPassword());
            Role role = entityFinderService.getRoleByName(registrationBody.getRole());

            User user = new User();
            user.setName(registrationBody.getName());
            user.setUsername(registrationBody.getUsername());
            user.setEmail(registrationBody.getEmail());
            user.setPassword(encodedPassword);
            user.setRole(role);
            user.setEnabled(true);
            user.setAccountExpired(false);
            user.setAccountLocked(false);
            user.setCredentialsExpired(false);
            user.setCarts(new ArrayList<>());

            User userSaved = userRepository.save(user);
            Cart newCart = cartService.createCart(userSaved);
            userSaved.getCarts().add(newCart);

        } catch (DataIntegrityViolationException e) {
            throw new UserRegistrationException("Database integrity error during user registration", e);
        } catch (Exception e) {
            throw new UserRegistrationException("Unexpected error during user registration", e);
        }
    }

    /**
     * Obtiene la información de un usuario.
     * @param userId Identificador del usuario.
     * @return Objeto que contiene la información del usuario.
     */
    public UserDto getUser(Long userId){
        User user = entityFinderService.getUserById(userId);

        return UserDto.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .build();
    }
}