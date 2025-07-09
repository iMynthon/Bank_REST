package com.example.bankcards.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record IsActiveRequest(
        UUID userId,
        String numberCard,
        Boolean isActive
) {
}
