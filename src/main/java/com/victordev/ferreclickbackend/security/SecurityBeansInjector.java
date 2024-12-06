package com.victordev.ferreclickbackend.security;

import com.victordev.ferreclickbackend.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Esta clase se encarga de inyectar los beans necesarios para la configuración de seguridad.
 */
@Configuration
public class SecurityBeansInjector {

    /**
     * Configuración de autenticación.
     */
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    /**
     * Repositorio de usuarios.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Se encarga de inyectar el AuthenticationManager para la autenticación de los usuarios.
     * @return AuthenticationManager para la autenticación de los usuarios.
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Se encarga de inyectar el UserDetailsService para la autenticación de los usuarios.
     * @return UserDetailsService para la autenticación de los usuarios.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            return userRepository.findByUsernameIgnoreCase(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found [" + username + "]"));
        };
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

    /**
     * Se encarga de inyectar el AuthenticationProvider para la autenticación de los usuarios.
     * @return AuthenticationProvider para la autenticación de los usuarios.
     * @throws Exception
     */
    @Bean
    public AuthenticationProvider authenticationProvider() throws Exception {
        DaoAuthenticationProvider provider= new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }
}