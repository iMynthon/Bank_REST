package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(
        @NotBlank(message = "В запросе не указан номер телефона - phoneNumber")
        @Pattern(message = "Формат введенного вами номера не валидный", regexp = "^([78])[\\s\\-]?\\(?\\d{3}\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$")
        String phoneNumber,
        @NotBlank(message = "В запросе не задан пароль - password")
        String password
) {
}
