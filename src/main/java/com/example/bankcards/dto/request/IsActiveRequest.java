package com.example.bankcards.dto.request;

import lombok.Builder;

@Builder
public record IsActiveRequest(
        String numberCard,
        Boolean isActive
) {
}
