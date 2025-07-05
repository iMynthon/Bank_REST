package com.example.bankcards.dto.response;

import org.springframework.data.domain.Page;

public record AllUserResponse(
        Page<UserResponse> users
) {
}
