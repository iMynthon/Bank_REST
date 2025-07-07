package com.example.bankcards.dto.request;

import com.example.bankcards.model.RoleType;
import lombok.Builder;

@Builder
public record UserRequest(
        String firstName,
        String lastName,
        String patronymic,
        String phoneNumber,
        String password,
        RoleType role
) {

}
