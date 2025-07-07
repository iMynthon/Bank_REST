package com.example.bankcards.dto.response;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDateTime registerTime
) {
}
