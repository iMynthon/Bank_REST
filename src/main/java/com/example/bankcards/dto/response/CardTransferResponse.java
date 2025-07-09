package com.example.bankcards.dto.response;
import com.example.bankcards.model.StatusTransfer;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder
public record CardTransferResponse(
        String sourceCard,
        String targetCard,
        BigDecimal amount,
        LocalDateTime transferTime,
        StatusTransfer statusTransfer
) {
}
