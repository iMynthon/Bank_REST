package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CardControllerTest extends AbstractTest {

    @Test
    @DisplayName("Тест на получение своих карт")
    void testGetAllMeCards() throws Exception {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), phoneNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        mockMvc.perform(get("/api/v1/card/me")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listCards.content.length()").value(3))
                .andDo(print());
    }

    @Test
    @DisplayName("Тест выгрузка всех карт с разными правами")
    void testGetAllCards() throws Exception {
        User user = userRepository.findByPhoneNumber(adminNumber).orElseThrow();
        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), adminNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        mockMvc.perform(get("/api/v1/card/me")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
