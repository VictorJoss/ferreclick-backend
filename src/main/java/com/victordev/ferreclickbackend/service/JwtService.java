package com.victordev.ferreclickbackend.service;

import io.jsonwebtoken.Claims;
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

@Service
public class JwtService {

    @Value("${security.jwt.EXPIRATION_IN_MINUTES}")
    private long EXPIRATION_IN_MINUTES;
    @Value("${security.jwt.SECRET_KEY}")
    private String SECRET_KEY;

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

    private SecretKey generateKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractAllClaims(String jwt){
        return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(jwt).getPayload();
    }

    public String extractSubject(String jwt){
        return extractAllClaims(jwt).getSubject();
    }
}
