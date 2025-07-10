package com.example.bankcards.dto.response;

import com.example.bankcards.model.PaymentSystem;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CardResponse {
    private String numberCard;
    private PaymentSystem paymentSystem;
    private BigDecimal score;
    private String owner;
    private String validityPeriod;
    private Boolean isActive;
}