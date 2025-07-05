package com.example.bankcards.dto.response;

public record ErrorResponse(
        int code,
        String message
) {
}
