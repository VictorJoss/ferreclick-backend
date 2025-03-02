package com.victordev.ferreclickbackend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * Servicio que proporciona métodos para generar y extraer tokens JWT.
 */
@Service
public class JwtService {

    /**
     * Clave secreta para firmar los tokens.
     */
    @Value("${security.jwt.EXPIRATION_IN_MINUTES}")
    private long EXPIRATION_IN_MINUTES;
    /**
     * Tiempo de expiración de los tokens en minutos.
     */
    @Value("${security.jwt.SECRET_KEY}")
    private String SECRET_KEY;

    /**
     * Genera un token JWT.
     * @param user Detalles del usuario.
     * @param extraClaims Reclamaciones adicionales del token.
     * @return Token JWT generado.
     */
    public String generateToken(UserDetails user, Map<String, Object> extraClaims){

        Date issueAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issueAt.getTime() + (EXPIRATION_IN_MINUTES * 60 * 1000));
        Key key = generateKey();


        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(issueAt)
                .expiration(expiration)
                .header()
                .type("JWT")
                .and()
                .signWith(key)
                .compact();
    }

    /**
     * Genera una clave secreta.
     * @return Clave secreta generada.
     */
    private SecretKey generateKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Extrae todas las reclamaciones de un token JWT.
     * @param jwt Token JWT.
     * @return Reclamaciones extraídas.
     */
    public Claims extractAllClaims(String jwt){
        return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(jwt).getPayload();
    }

    /**
     * Extrae el nombre de usuario de un token JWT.
     * @param jwt Token JWT.
     * @return Nombre de usuario extraído.
     */
    public String extractSubject(String jwt){
        return extractAllClaims(jwt).getSubject();
    }

    /**
     * Valida si un token JWT es válido.
     * @param jwt Token JWT a validar.
     * @param userDetails Detalles del usuario.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        try {
            final String username = extractSubject(jwt);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Verifica si un token JWT ha expirado.
     * @param jwt Token JWT a verificar.
     * @return true si el token ha expirado, false en caso contrario.
     */
    public boolean isTokenExpired(String jwt) {
        return extractAllClaims(jwt).getExpiration().before(new Date());
    }
}
