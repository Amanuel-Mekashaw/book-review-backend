package com.project.bookreviewapp.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "EKO/tIYyoEPEhNIEE0yvxs0dlhYtDL5gTKbRvOdJk6E=";// EKO/tIYyoEPEhNIEE0yvxs0dlhYtDL5gTKbRvOdJk6E=
    private static final long EXPIRATION_TIME_MS = 5 * 24 * 60 * 60 * 1000; // 5 days in milliseconds

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();

        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst()
                .orElse("ADMIN");

        extraClaims.put("role", role);
        return generateToken(extraClaims, userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // final String role = extractRole(token).toUpperCase();
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extratExpiration(token).before(new Date());
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    private Date extratExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try {

            return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
        } catch (RuntimeException ex) {
            throw new RuntimeException("Error generating JWT token", ex);
        }

    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            throw new UnsupportedJwtException("Invalid or unsupported JWT token", e);
        }

    }

    private Key getSignInKey() {
        byte[] keybytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keybytes);
    }

}
