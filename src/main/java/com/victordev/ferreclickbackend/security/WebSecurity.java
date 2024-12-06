package com.victordev.ferreclickbackend.security;

import com.victordev.ferreclickbackend.web.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Esta clase se encarga de configurar la seguridad de la aplicación.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurity{

    /**
     * Filtro de autenticación JWT.
     */
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Se encarga de configurar la seguridad de la aplicación.
     * @param http HttpSecurity para la configuración de la seguridad.
     * @return SecurityFilterChain para la configuración de la seguridad.
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        return http
                .csrf(config -> config.disable())
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
