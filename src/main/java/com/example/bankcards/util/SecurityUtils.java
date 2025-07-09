package com.example.bankcards.util;

import com.example.bankcards.security.AppUserPrincipal;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {
    public static UUID userId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal appUserPrincipal = (AppUserPrincipal) authentication.getPrincipal();
        return appUserPrincipal.id();
    }
}
