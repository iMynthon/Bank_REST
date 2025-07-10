package com.example.bankcards.security;

import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserDetails implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return new AuthenticatedDetails(userService.findByPhoneNumber(phoneNumber));
    }
}
