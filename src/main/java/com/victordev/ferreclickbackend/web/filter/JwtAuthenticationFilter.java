package com.victordev.ferreclickbackend.web.filter;

import com.victordev.ferreclickbackend.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Servicio de JWT.
     */
    private final JwtService jwtService;

    /**
     * Servicio de detalles de usuario.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Verifica si el token JWT es válido y autentica al usuario.
     * @param request Petición HTTP.
     * @param response Respuesta HTTP.
     * @param filterChain Cadena de filtros.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwt = extractJwtFromRequest(request);
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String subject = jwtService.extractSubject(jwt);

            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(subject);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    System.out.println(authToken.getDetails());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (JwtException e) {
            logger.error("JWT token validation failed: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT de la petición HTTP.
     * @param request Petición HTTP.
     * @return Token JWT extraído.
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
