package com.example.bankcards.dto.response;

import java.util.List;

public record AllCardTransferResponse(
        List<CardTransferResponse> responseList
) {
}
