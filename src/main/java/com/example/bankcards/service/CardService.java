package com.example.bankcards.service;

import com.example.bankcards.dto.response.AllCardResponse;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Transactional(readOnly = true)
    public AllCardResponse findByCardsToUser(){
          return cardMapper.listCardToListResponse(cardRepository.findCardByUserId(UUID.randomUUID()));
    }
}
