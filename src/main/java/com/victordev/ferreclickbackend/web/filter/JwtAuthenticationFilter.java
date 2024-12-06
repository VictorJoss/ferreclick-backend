package com.victordev.ferreclickbackend.web.filter;

import com.victordev.ferreclickbackend.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que maneja la autenticación con JWT.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Servicio de JWT.
     */
    @Autowired
    private JwtService jwtService;

    /**
     * Servicio de detalles de usuario.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Verifica si el token JWT es válido y autentica al usuario.
     *
     * @param request     Petición HTTP.
     * @param response    Respuesta HTTP.
     * @param filterChain Cadena de filtros.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        if(!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.split("Bearer ")[1];
        String subject = null;

        try {
            subject = jwtService.extractSubject(jwt);
        }catch (JwtException e){
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails user = userDetailsService.loadUserByUsername(subject);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user,
                null,
                user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
