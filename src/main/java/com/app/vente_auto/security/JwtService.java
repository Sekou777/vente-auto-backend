package com.app.vente_auto.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

     private  final Key SECRET_KEY ;
    private  final long EXPIRATION_TIME;

    // Injection des valeurs de configuration
    public JwtService(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration}") long expirationTime
    ) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
        this.EXPIRATION_TIME = expirationTime;
    }

    // Génère un token JWT à partir d'un utilisateur
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims) 
                .setSubject(email) // identifiant principal
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                 .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrait l'email du token JWT
    public String extractEmail(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject(); // l'email a été mis dans le .setSubject() lors de la génération
    }

    // Vérifie si le token est valide pour l'utilisateur
    public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

   
    // refresh token
    public String generateRefreshToken(String email) {
    return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 jours
            .signWith(SECRET_KEY , SignatureAlgorithm.HS256)
            .compact();
}
}

/*
 *  Explication rapide :

setSubject(email) → le token JWT est lié à l'email de l’utilisateur

setIssuedAt() → date de création

setExpiration() → date d’expiration

signWith() → algorithme utilisé (ici : HMAC SHA-256)

Une méthode pour vérifier si un token est bien valide (isTokenValid)

Des outils pour extraire les infos du token (extractUsername, isTokenExpired, extractAllClaims)
 */