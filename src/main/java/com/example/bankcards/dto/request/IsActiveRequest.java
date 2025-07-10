package com.example.bankcards.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record IsActiveRequest(
        @NotNull(message = "В запросе не указан идентификатор пользователя - userId")
        UUID userId,
        @NotBlank(message = "В запросе не указан номер карты - numberCard")
        String numberCard,
        @NotNull(message = "В запросе не указано активировать или заблокировать карту - isActive")
        Boolean isActive
) {
}
