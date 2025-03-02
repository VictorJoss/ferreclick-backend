package com.victordev.ferreclickbackend.security;

import com.victordev.ferreclickbackend.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Esta clase se encarga de inyectar los beans necesarios para la configuración de seguridad.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityBeansInjector {

    /**
     * Repositorio de usuarios.
     */
    private final UserRepository userRepository;

    /**
     * Se encarga de inyectar el AuthenticationManager para la autenticación de los usuarios.
     * @return AuthenticationManager para la autenticación de los usuarios.
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    /**
     * Se encarga de inyectar el UserDetailsService para la autenticación de los usuarios.
     * @return UserDetailsService para la autenticación de los usuarios.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found [" + username + "]"));
    }

    /**
     * Se encarga de inyectar el PasswordEncoder para la encriptación de las contraseñas.
     * @return PasswordEncoder para la encriptación de las contraseñas.
     */
    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}