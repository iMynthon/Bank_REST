package com.example.bankcards.service.security;

import com.example.bankcards.exception.TokenParseException;
import com.example.bankcards.model.Role;
import com.example.bankcards.model.RoleType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class JwtTokenService {

    public static final String ROLES_CLAIM = "roles";
    public static final String ID_CLAIM = "id";

    @Value("${bank-rest.jwt.secret}")
    private String jwtSecret;
    @Value("${bank-rest.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret.trim());
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA512");
    }

    public String generatedToken(UUID userId, String phoneNumber, List<String> roles){
        return Jwts.builder()
                .setSubject(phoneNumber)
                .setExpiration(Date.from(Instant.now().plus(tokenExpiration)))
                .claim(ID_CLAIM,userId)
                .claim(ROLES_CLAIM,roles)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validate(String authToken) {
        try {
            parseTokenClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Токен недействителен: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Срок действия истек: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Неподдерживаемый формат токена: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Claims пустые: {}", e.getMessage());
        }

        return false;
    }

    public Claims parseTokenClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateClaims(String email, String id, List<Role> roles) {
        if (email == null || id == null || roles == null) {
            log.error("Missing required claims - Email: {}, ID: {}, Roles: {}", email, id, roles);
            throw new TokenParseException("Token claims cannot be null");
        }

        if (email.isBlank() || id.isBlank() || roles.isEmpty()) {
            log.error("Empty claims - Email: {}, ID: {}, Roles: {}", email, id, roles);
            throw new TokenParseException("Token claims cannot be empty");
        }
    }
}
