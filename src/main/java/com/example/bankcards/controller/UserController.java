package com.example.bankcards.controller;

import com.example.bankcards.dto.request.UserRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
