package com.example.bankcards.dto.request;

import com.example.bankcards.model.PaymentSystem;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CardRequest(
        UUID userId,
        String numberCard,
        PaymentSystem paymentSystem
) {
}
