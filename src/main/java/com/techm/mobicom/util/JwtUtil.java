package com.techm.mobicom.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // SecretKey for signing and verifying JWT
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        logger.debug("Secret key length: {} bits", keyBytes.length * 8);
        if (keyBytes.length * 8 < 256) {
            logger.warn("Secret key is too short for HS256 ({} bits). Minimum required is 256 bits.", keyBytes.length * 8);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        String principal = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        logger.info("Creating token for principal: {}, role: {}", principal, role);
        return createToken(claims, principal);
    }

    public String generateToken(String principal, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("email", email);
        logger.info("Creating token for principal: {}, email: {}, role: {}", principal, email, role);
        return createToken(claims, principal);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        logger.info("Generating JWT token for subject: {}", subject);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        logger.debug("Parsing token with secret key (length: {} bits)", secret.getBytes(StandardCharsets.UTF_8).length * 8);
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            logger.error("Failed to parse JWT token: {}", e.getMessage());
            throw e;
        }
    }

    public Boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            boolean usernameMatches = extractedUsername.equals(username);
            boolean isNotExpired = !isTokenExpired(token);

            if (usernameMatches && isNotExpired) {
                logger.info("JWT token is valid for username: {}", username);
                return true;
            } else {
                logger.warn("JWT token validation failed: usernameMatches={}, isNotExpired={}", usernameMatches, isNotExpired);
                return false;
            }
        } catch (JwtException e) {
            logger.error("Error validating JWT token: {}", e.getMessage());
            return false;
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}