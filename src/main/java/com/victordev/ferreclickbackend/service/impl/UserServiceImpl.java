package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.DTOs.api.user.RegistrationBody;
import com.victordev.ferreclickbackend.DTOs.api.user.UserDto;
import com.victordev.ferreclickbackend.DTOs.security.LoginRequest;
import com.victordev.ferreclickbackend.DTOs.security.LoginResponse;
import com.victordev.ferreclickbackend.exceptions.user.UserAlreadyExistsException;
import com.victordev.ferreclickbackend.persistence.entity.Cart;
import com.victordev.ferreclickbackend.persistence.entity.Role;
import com.victordev.ferreclickbackend.persistence.entity.User;
import com.victordev.ferreclickbackend.persistence.repository.RoleRepository;
import com.victordev.ferreclickbackend.persistence.repository.UserRepository;
import com.victordev.ferreclickbackend.service.ICartService;
import com.victordev.ferreclickbackend.service.IUserService;
import com.victordev.ferreclickbackend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
     * Repositorio de roles.
     */
    private final RoleRepository roleRepository;
    /**
     * Codificador de contraseñas.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * Configuración de seguridad HTTP.
     */
    private final HttpSecurity http;
    /**
     * Detalles del usuario.
     */
    private final UserDetailsService userDetailsService;
    /**
     * Gestor de autenticación.
     */
    private final AuthenticationManager authenticationManager;
    /**
     * Servicio de JWT.
     */
    private final JwtService jwtService;
    /**
     * Servicio de carrito.
     */
    private final ICartService cartService;

    /**
     * Registra un nuevo usuario.
     * @param registrationBody Objeto que contiene la información del usuario a registrar.
     * @return Objeto que contiene la información del usuario registrado.
     */
    @Override
    public User registerUser(RegistrationBody registrationBody) throws UserAlreadyExistsException {

        if (userRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()
        || userRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        String encodedPassword = passwordEncoder.encode(registrationBody.getPassword());

        Role role = roleRepository.findByName(Role.RoleEnum.valueOf(registrationBody.getRole()))
                .orElseThrow(() -> new RuntimeException("Role not found"));

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

        User userSaved = userRepository.save(user);
        Cart newCart = cartService.createCart(userSaved);

        userSaved.getCarts().add(newCart);

        return userSaved;
    }

    /**
     * Autentica un usuario.
     * @param loginRequest Objeto que contiene la información del usuario a autenticar.
     * @return Objeto que contiene el token de autenticación.
     */
    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {

        Optional<User> OpUser = userRepository.findByEmailIgnoreCase(loginRequest.email());
        if (OpUser.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = OpUser.get();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                loginRequest.password(),
                userDetails.getAuthorities()
        );

        authenticationManager.authenticate(authentication);

        String jwt = jwtService.generateToken(userDetails, generateExtraClaims(userDetails));
        return new LoginResponse(jwt);
    }

    /**
     * Genera los claims adicionales que se incluirán en el token JWT.
     * @param userDetails Detalles del usuario autenticado.
     * @return Mapa con los claims adicionales.
     */
    private Map<String, Object> generateExtraClaims(UserDetails userDetails){
        Map<String, Object> extraClaims = new HashMap<>();

        User user = (User) userDetails;
        String roleName = user.getRole().getName().name();

        extraClaims.put("role", roleName);
        extraClaims.put("authorities",
                user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));
        extraClaims.put("userId", user.getId());
        return extraClaims;
    }

    /**
     * Obtiene la información de un usuario.
     * @param userId Identificador del usuario.
     * @return Objeto que contiene la información del usuario.
     */
    public UserDto getUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: "+ userId));

        return UserDto.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .build();
    }

    /**
     * Cierra la sesión de un usuario.
     */
    public void logout(){
        try{
            http.logout(httpSecurityLogoutConfigurer -> {
                httpSecurityLogoutConfigurer.deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true);
            });
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
