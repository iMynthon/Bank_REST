package com.example.bankcards.controller;

import com.example.bankcards.dto.response.AllUserResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin")
public class AdminController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public AllUserResponse getAllUsers(@PageableDefault Pageable pageable){
        return userService.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/user")
    public UserResponse findByUser(@PathVariable UUID userId){
        return userService.findByUser(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable UUID userId){
        return userService.delete(userId);
    }
}
