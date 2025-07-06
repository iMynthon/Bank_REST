package com.example.bankcards.controller;

import com.example.bankcards.dto.request.IsActiveRequest;
import com.example.bankcards.dto.request.UserRequest;
import com.example.bankcards.dto.response.AllUserResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public UserResponse findByMeUser(){
        return userService.findByUser();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/register")
    public UserResponse save(@RequestBody UserRequest request){
       return userService.save(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/me")
    public UserResponse update(@RequestBody UserRequest request){
        return userService.update(request);
    }

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
