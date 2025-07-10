package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "В запросе не указан номер телефона - phoneNumber")
        String phoneNumber,
        @NotBlank(message = "В запросе не задан пароль - password")
        String password
) {
}
