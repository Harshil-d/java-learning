package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {

    private final Key signingKey;
    private final long expirationMillis;

    public JwtService(
        @Value("${security.jwt.secret}") String secret,
        @Value("${security.jwt.expiration-ms:3600000}") long expirationMillis
    ) {
        try {
            System.out.println("harshil: test 1");
            this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
            System.out.println("harshil: test 2");
            this.expirationMillis = expirationMillis;
            System.out.println("harshil: test 3");
        } catch (Exception e) {
            System.out.println("harshil: error " + e.getMessage());
            throw e;
        }
    }

    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .addClaims(Map.of(
                "roles",
                userDetails.getAuthorities().stream()
                    .map(Object::toString)
                    .toList()
            ))
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusMillis(expirationMillis)))
            .signWith(signingKey)
            .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
