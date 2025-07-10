package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.PostgresTestContainerInitializer;
import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.request.UserRequest;
import com.example.bankcards.model.RoleType;
import com.example.bankcards.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class AuthorizationControllerTest extends AbstractTest {

    @Test
    @DisplayName("Тест -> регистрация пользователя")
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
    @DisplayName("Тест -> логин пользователя")
    void testLoginEndpoints() throws Exception{
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();

        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), phoneNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        LoginRequest request = new LoginRequest(user.getPhoneNumber(),"emma789");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                        .header("Authorization",token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест -> логин пользователя c неверными данными")
    void testLoginEndpointsException() throws Exception{
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();

        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), phoneNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        LoginRequest request = new LoginRequest(user.getPhoneNumber(),"12121212121212121");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization",token))
                .andExpect(status().isForbidden());
    }

}
