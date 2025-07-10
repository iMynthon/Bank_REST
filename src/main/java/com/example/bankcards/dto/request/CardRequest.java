package com.example.bankcards.dto.request;

import com.example.bankcards.model.PaymentSystem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CardRequest(
        @NotNull(message = "При запросе не указан идентификатор пользователя - userId")
        UUID userId,
        @NotNull(message = "Не указана платженая система - paymentSystem")
        PaymentSystem paymentSystem
) {
}
