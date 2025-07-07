package com.example.bankcards.security;
import com.example.bankcards.model.RoleType;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public record AppUserPrincipal(
        UUID id,
        String phoneNumber,
        List<RoleType> role
) implements Principal {
    @Override
    public String getName() {
        return phoneNumber;
    }
}
