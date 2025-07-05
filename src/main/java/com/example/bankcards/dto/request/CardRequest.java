package com.example.bankcards.dto.request;

import com.example.bankcards.model.PaymentSystem;
import lombok.Builder;

@Builder
public record CardRequest(
        String numberCard,
        PaymentSystem paymentSystem
) {
}
