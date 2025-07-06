package com.example.bankcards.dto.response;

import com.example.bankcards.model.PaymentSystem;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CardResponse {
    private String numberCard;
    private PaymentSystem paymentSystem;
    private String owner;
    private String validityPeriod;
}