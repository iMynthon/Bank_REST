package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.request.UserRequest;
import com.example.bankcards.model.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class AuthorizationControllerTest extends AbstractTest {

    @Test
    @DisplayName("Тест регистрация пользователя")
    void testRegisterEndpoint() throws Exception{

        UserRequest userRequest = UserRequest.builder()
                .firstName("Json")
                .lastName("Curly")
                .patronymic("Duhovich")
                .phoneNumber("79112657890")
                .role(RoleType.ROLE_USER)
                .password("121212ff")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Тест логин пользователя")
    void testLoginEndpoints() throws Exception{

        LoginRequest request = new LoginRequest("9319206789","12121212121");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

}
