package com.example.bankcards.dto.response;
import com.example.bankcards.model.StatusTransfer;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder
public record CardTransferResponse(
        String hashSourceCard,
        String hashTargetCard,
        BigDecimal amount,
        LocalDateTime transferTime,
        StatusTransfer statusTransfer
) {
}
