package com.example.bankcards.service.security;

import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.model.jwt.RefreshToken;
import com.example.bankcards.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtRefreshService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${bank-rest.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    public RefreshToken save(UUID userId){
        RefreshToken refreshToken = generateNewToken(userId);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken generateNewToken(UUID userId) {
        return RefreshToken.builder()
                .id(UUID.randomUUID().toString())
                .value(generateSecureToken())
                .userId(userId)
                .expiration(refreshTokenExpiration.toSeconds())
                .build();
    }

    public String generateSecureToken() {
        byte[] bytes = new byte[36];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
