package com.example.bankcards.controller;
import com.example.bankcards.AbstractTest;
import com.example.bankcards.PostgresTestContainerInitializer;
import com.example.bankcards.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class UserControllerTest extends AbstractTest {

    @Test
    @DisplayName("Тест -> получение данных своего профиля")
    public void testGetUserMeData() throws Exception {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();

        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), phoneNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());

        mockMvc.perform(get("/api/v1/user/me")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andDo(print());

    }

    @Test
    @DisplayName("Тест -> запрос всех пользователей с ролью админа и обычного пользователя")
    public void testGetAllUsers() throws Exception{
        User user = userRepository.findByPhoneNumber(adminNumber).orElseThrow();
        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), adminNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());

        mockMvc.perform(get("/api/v1/user/all")
                        .header("Authorization",token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users.content.length()").value(6))
                .andExpect(jsonPath("$.users.content[*]").value(everyItem(notNullValue())))
                .andExpect(jsonPath("$.users.totalElements").value(6))
                .andExpect(jsonPath("$.users.size").value(10))
                .andDo(print());

        User user1 = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        String token1 = "Bearer " + jwtTokenService
                .generatedToken(user1.getId(), phoneNumber, user1.getRoles().stream().map(role -> role.getRoleType().toString()).toList());

        mockMvc.perform(get("/api/v1/user/all")
                        .header("Authorization",token1))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    @DisplayName("Тест -> поиск по идентификатору пользователя с правами админа и обычного пользователя")
    void testGetFindUserById() throws Exception{
        User user = userRepository.findByPhoneNumber(adminNumber).orElseThrow();
        User user1 = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), adminNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());

        mockMvc.perform(get("/api/v1/user/{userId}",user1.getId())
                        .header("Authorization",token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1.getId().toString()))
                .andExpect(jsonPath("$.phoneNumber").value(user1.getPhoneNumber()))
                .andExpect(jsonPath("$.firstName").value(user1.getFirstName()))
                .andDo(print());

        String token1 = "Bearer " + jwtTokenService
                .generatedToken(user1.getId(), phoneNumber, user1.getRoles().stream().map(role -> role.getRoleType().toString()).toList());

        mockMvc.perform(get("/api/v1/user/{userId}",user.getId())
                        .header("Authorization",token1))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("Тест -> удаление пользователя по идентификатору с правами админа и обычного пользователя")
    void testDeleteUser() throws Exception{
        User user = userRepository.findByPhoneNumber(adminNumber).orElseThrow();
        User user1 = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), adminNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());

        assertEquals(6,userRepository.count());

        mockMvc.perform(delete("/api/v1/user/{userId}/delete",user1.getId())
                        .header("Authorization",token))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(5,userRepository.count());

        String token1 = "Bearer " + jwtTokenService
                .generatedToken(user1.getId(), phoneNumber, user1.getRoles().stream().map(role -> role.getRoleType().toString()).toList());

        mockMvc.perform(delete("/api/v1/user/{userId}/delete",user.getId())
                        .header("Authorization",token1))
                .andExpect(status().isForbidden())
                .andDo(print());

        assertEquals(5,userRepository.count());
    }
}
