package com.example.bankcards.dto.request;

import java.math.BigDecimal;

public record CardTransferRequest(
        String sourceCard,
        String targetCard,
        BigDecimal amount
) {
}
