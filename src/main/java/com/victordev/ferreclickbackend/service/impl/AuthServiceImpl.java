package com.victordev.ferreclickbackend.service.impl;

import com.victordev.ferreclickbackend.DTOs.security.LoginRequest;
import com.victordev.ferreclickbackend.DTOs.security.LoginResponse;
import com.victordev.ferreclickbackend.exceptions.security.AuthUserException;
import com.victordev.ferreclickbackend.exceptions.security.IllegalUserException;
import com.victordev.ferreclickbackend.exceptions.user.UserNotFoundException;
import com.victordev.ferreclickbackend.persistence.entity.User;
import com.victordev.ferreclickbackend.persistence.repository.UserRepository;
import com.victordev.ferreclickbackend.service.IAuthService;
import com.victordev.ferreclickbackend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación del servicio de autenticación que proporciona métodos para autenticar y cerrar la sesión de un usuario.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
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

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {

        Optional<User> OpUser = userRepository.findByEmailIgnoreCase(loginRequest.email());
        if (OpUser.isEmpty()) {
            throw new UserNotFoundException("The user with the email address provided does not exist.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(OpUser.get().getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            loginRequest.password(),
                            userDetails.getAuthorities()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtService.generateToken(userDetails, generateExtraClaims(userDetails));

            User authenticatedUser = getAuthenticatedUser();
            System.out.println("Usuario autenticado: " + authenticatedUser.getUsername());

            return new LoginResponse(jwt);
        } catch (BadCredentialsException e) {
            throw new IllegalUserException("Invalid email or password");
        } catch (Exception e) {
            throw new AuthUserException("Unexpected error in the authentication process.");
        }
    }

    /**
     * Cierra la sesión de un usuario.
     */
    @Override
    public void logout() {
        try {
            http.logout(httpSecurityLogoutConfigurer -> {
                httpSecurityLogoutConfigurer.deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true);
            });
        } catch (Exception e) {
            throw new AuthUserException("Error logging out.");
        }
    }

    /**
     * Verifica si el usuario autenticado es el mismo que se intenta modificar.
     * @param userId Identificador del usuario a modificar.
     * @return true si el usuario autenticado es el mismo que se intenta modificar, false en caso contrario.
     */
    @Override
    public boolean isLegalUser(Long userId) {
        return !getAuthenticatedUser().getId().equals(userId);
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
                        .toList());
        extraClaims.put("userId", user.getId());
        return extraClaims;
    }

    /**
     * Obtiene el usuario autenticado.
     * @return Usuario autenticado.
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("No hay un usuario autenticado");
        }

        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException("User not found [" + username + "]"));
    }
}
