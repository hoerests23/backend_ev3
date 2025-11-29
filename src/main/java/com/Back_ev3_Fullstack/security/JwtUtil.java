package com.Back_ev3_Fullstack.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    // Clave secreta fija (debe ser 256 bits para HS256)
    private static final String SECRET = "12345678901234567890123456789012";

    private final Key signingKey;

    public JwtUtil() {
        this.signingKey = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 horas
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractCorreo(String token) {
        return extractAllClaims(token).getSubject();
    }

    public List<String> extractRoles(String token){
        return extractAllClaims(token).get("roles", List.class);
    }
}
