package com.Back_ev3_Fullstack.service;

import com.Back_ev3_Fullstack.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Set;

public class JwtService {
    private final String SECRET_KEY = "12345678901234567890123456789012"; // guarda en properties idealmente
    private final long EXPIRATION = 1000 * 60 * 60; // 1 hora

    public String generarToken(String correo, Set<Role> roles) {
        return Jwts.builder()
                .setSubject(correo)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

}
