package com.example.bankcards.controller;
import com.example.bankcards.aop.LogController;
import com.example.bankcards.dto.request.UserRequest;
import com.example.bankcards.dto.response.AllUserResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
@LogController
@Validated
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserResponse findByMeUser(){
        return userService.findByUserSecurityContext();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public UserResponse update(@RequestBody @Valid UserRequest request){
        return userService.update(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public AllUserResponse getAllUsers(@PageableDefault Pageable pageable){
        return userService.findAll(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public UserResponse findByUser(@PathVariable(name = "userId") @NotNull UUID userId){
        return userService.findByUserId(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{userId}/delete")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String deleteUser(@PathVariable(name = "userId") @NotNull UUID userId){
        return userService.delete(userId);
    }
}
