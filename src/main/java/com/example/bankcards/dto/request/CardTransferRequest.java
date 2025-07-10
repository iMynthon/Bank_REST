package com.example.bankcards.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CardTransferRequest(
        @NotBlank(message = "В запросе не указан номер карты с который собираетесь сделать перевод - hashSourceCard")
        String hashSourceCard,
        @NotBlank(message = "В запросе не указан номер карты на которую собираетесь сделать перевод - hashTargetCard")
        String hashTargetCard,
        @NotNull(message = "В запросе не указана сумма перевода - amount")
        @DecimalMin(value = "0.01", message = "Минимальная сумма перевода - {value}")
        BigDecimal amount
) {
}
