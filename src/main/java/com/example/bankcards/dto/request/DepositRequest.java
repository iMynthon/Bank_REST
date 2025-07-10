package com.example.bankcards.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositRequest(
        @NotBlank(message = "В запросе не указан номер карты - numberCard")
        String numberCard,
        @NotNull(message = "В запросе не указана сумма перевода - amount")
        @DecimalMin(value = "0.01", message = "Минимальная сумма перевода - {value}")
        BigDecimal amount
) {
}
