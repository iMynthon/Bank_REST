package com.example.bankcards.dto.request;

public record LoginRequest(
        String phoneNumber,
        String password
) {
}
