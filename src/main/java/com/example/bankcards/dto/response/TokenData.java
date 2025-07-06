package com.example.bankcards.dto.response;

public record TokenData(
        String accessToken,
        String refreshToken
) {
}
