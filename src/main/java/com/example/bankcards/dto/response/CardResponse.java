package com.example.bankcards.dto.response;

import com.example.bankcards.model.PaymentSystem;
import lombok.Builder;

@Builder
public record CardResponse(
        String numberCard,
        PaymentSystem paymentSystem,
        String owner,
        String validityPeriod
) {
}
