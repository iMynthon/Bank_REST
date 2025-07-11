package com.example.bankcards.controller;

import com.example.bankcards.AbstractTest;
import com.example.bankcards.PostgresTestContainerInitializer;
import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.request.CardTransferRequest;
import com.example.bankcards.dto.request.DepositRequest;
import com.example.bankcards.dto.request.IsActiveRequest;
import com.example.bankcards.model.Card;
import com.example.bankcards.model.PaymentSystem;
import com.example.bankcards.model.User;
import com.example.bankcards.util.StringCreatorUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(initializers = PostgresTestContainerInitializer.class)
public class CardControllerTest extends AbstractTest {

    @Test
    @DisplayName("Тест -> запрос на получения списка своих карт")
    void testGetAllMeCards() throws Exception {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), phoneNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        mockMvc.perform(get("/api/v1/card/me")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listCards.content.length()").value(3))
                .andExpect(jsonPath("$.listCards.content.[0].paymentSystem").value(PaymentSystem.VISA.toString()))
                .andExpect(jsonPath("$.listCards.content.[1].paymentSystem").value(PaymentSystem.MASTERCARD.toString()))
                .andExpect(jsonPath("$.listCards.content.[2].paymentSystem").value(PaymentSystem.MIR.toString()))
                .andDo(print());
    }

    @Test
    @DisplayName("Тест -> выгрузка всех карт с разными правами")
    void testGetAllCards() throws Exception {
        User user = userRepository.findByPhoneNumber(adminNumber).orElseThrow();
        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), adminNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        mockMvc.perform(get("/api/v1/card/all")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listCards.content.length()").value(6))
                .andDo(print());

        User user1 = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        String token1 = "Bearer " + jwtTokenService
                .generatedToken(user1.getId(), phoneNumber, user1.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        mockMvc.perform(get("/api/v1/card/all")
                        .header("Authorization", token1))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("Тест -> на создание карты c разными правами")
    void testCreateCards() throws Exception {
        User user = userRepository.findByPhoneNumber(adminNumber).orElseThrow();
        User user1 = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        String owner = StringCreatorUtils.createOwner(user1.getFirstName(),user1.getLastName(),user1.getPatronymic());
        CardRequest cardRequest = CardRequest.builder()
                .userId(user1.getId())
                .paymentSystem(PaymentSystem.MASTERCARD)
                .build();
        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), adminNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        mockMvc.perform(post("/api/v1/card/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(cardRequest))
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentSystem").value(PaymentSystem.MASTERCARD.toString()))
                .andExpect(jsonPath("$.owner").value(owner))
                .andExpect(jsonPath("$.numberCard").isNotEmpty())
                .andDo(print());

        CardRequest cardRequest1 = CardRequest.builder()
                .userId(user1.getId())
                .paymentSystem(PaymentSystem.MASTERCARD)
                .build();
        String token1 = "Bearer " + jwtTokenService
                .generatedToken(user1.getId(), adminNumber, user1.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        mockMvc.perform(post("/api/v1/card/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(cardRequest1))
                        .header("Authorization", token1))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("Тест -> пополнение счета карты")
    @Transactional(propagation = Propagation.SUPPORTS)
    void testDepositCard() throws Exception{
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), adminNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        DepositRequest request = new DepositRequest(StringCreatorUtils.hashCard("4444 4444 4444 4444"),new BigDecimal(400000));
        mockMvc.perform(put("/api/v1/card/me/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
                .header("Authorization",token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Тест -> активация и блокировка карты по запросу пользователя")
    @Transactional(propagation = Propagation.SUPPORTS)
    void testActiveAndBlock() throws Exception{
        User user = userRepository.findByPhoneNumber(adminNumber).orElseThrow();
        User user1 = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        IsActiveRequest isActiveRequest = IsActiveRequest.builder()
                .userId(user1.getId())
                .hashNumberCard(StringCreatorUtils.hashCard("4444 4444 4444 4444"))
                .isActive(false)
                .build();

        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), adminNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        mockMvc.perform(put("/api/v1/card/active")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(isActiveRequest))
                        .header("Authorization",token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberCard").value("**** **** **** 4444"))
                .andExpect(jsonPath("$.isActive").value(isActiveRequest.isActive()))
                .andDo(print());
    }

    @Test
    @DisplayName("Тест -> ошибка запроса активации уже активированный карты")
    @Transactional(propagation = Propagation.SUPPORTS)
    void testActiveTOActiveException() throws Exception{
        User user = userRepository.findByPhoneNumber(adminNumber).orElseThrow();
        User user1 = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        IsActiveRequest isActiveRequest = IsActiveRequest.builder()
                .userId(user1.getId())
                .hashNumberCard(StringCreatorUtils.hashCard("4444 4444 4444 4444"))
                .isActive(true)
                .build();
        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), adminNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        mockMvc.perform(put("/api/v1/card/active")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(isActiveRequest))
                        .header("Authorization",token))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    @DisplayName("Тест -> перевод между своими счетами и запрос перевода")
    void testTransferMeCards()throws Exception{
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), phoneNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        CardTransferRequest request = CardTransferRequest.builder()
                .amount(new BigDecimal(200000))
                .hashTargetCard(StringCreatorUtils.hashCard("4444 4444 4444 4444"))
                .hashSourceCard(StringCreatorUtils.hashCard("6666 6666 6666 6666"))
                .build();
        mockMvc.perform(post("/api/v1/card/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                .header("Authorization",token))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/api/v1/card/me/transfers")
                .header("Authorization",token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list.length()").value(1))
                .andDo(print());


    }

    @Test
    @DisplayName("Тест -> ошибка перевода на заблокированную карту")
    void testTransfersIsNotActive()throws Exception{
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow();
        String token = "Bearer " + jwtTokenService
                .generatedToken(user.getId(), phoneNumber, user.getRoles().stream().map(role -> role.getRoleType().toString()).toList());
        CardTransferRequest request = CardTransferRequest.builder()
                .amount(new BigDecimal(200000))
                .hashTargetCard(StringCreatorUtils.hashCard("4444 4444 4444 4444"))
                .hashSourceCard(StringCreatorUtils.hashCard("5555 5555 5555 5555"))
                .build();
        mockMvc.perform(post("/api/v1/card/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .header("Authorization",token))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

}
