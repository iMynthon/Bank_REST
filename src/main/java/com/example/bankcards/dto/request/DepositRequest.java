package com.example.bankcards.dto.request;

import java.math.BigDecimal;

public record DepositRequest(
        String numberCard,
        BigDecimal score
) {
}
