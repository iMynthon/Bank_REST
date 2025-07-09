package com.example.bankcards.dto.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CardTransferRequest(
        String sourceCard,
        String targetCard,
        BigDecimal amount
) {
}
