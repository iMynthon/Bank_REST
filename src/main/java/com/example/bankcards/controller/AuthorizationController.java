package com.example.bankcards.controller;

import com.example.bankcards.aop.LogController;
import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.request.UserRequest;
import com.example.bankcards.dto.response.TokenData;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import com.example.bankcards.service.security.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@LogController
@Validated
public class AuthorizationController {

    private final UserService userService;
    private final SecurityService securityService;


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/register")
    public UserResponse save(@RequestBody @Valid UserRequest request){
        return userService.save(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public TokenData login(@RequestBody @Valid LoginRequest request){
       return securityService.processPasswordToken(request);
    }
}
