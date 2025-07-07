package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.request.IsActiveRequest;
import com.example.bankcards.dto.response.AllCardResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.model.Card;
import com.example.bankcards.model.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.SecurityUtils;
import com.example.bankcards.util.StringMaskedUtils;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final StringEncryptor stringEncryptor;
    private final UserService userService;

    @Transactional(readOnly = true)
    public AllCardResponse findByCardsToUser(Pageable pageable){
          return cardMapper.listCardToListResponse(cardRepository.findCardByUserId(SecurityUtils.userId(),pageable));
    }

    @Transactional(readOnly = true)
    public CardResponse findByNumberCard(String numberCard){
        return cardMapper.entityToResponse(cardRepository.findByNumberCard(numberCard)
                .orElseThrow(()-> new EntityNotFoundException("У вас карты с таким номером")));
    }

    @Transactional
    public String isActiveRequest(IsActiveRequest request){
        cardRepository.IsActive(request.numberCard(),request.isActive());
        return String.format("Card number: %s - isActive - %s",request.numberCard(),request.isActive());
    }

    @Transactional
    public CardResponse save(CardRequest cardRequest) {
        User user = userService.findById();
        Card card = cardRepository.save(Card.builder()
                .numberCard(stringEncryptor.encrypt(cardRequest.numberCard()))
                .paymentSystem(cardRequest.paymentSystem())
                .isActive(false)
                .user(user)
                .build());
        CardResponse cardResponse = cardMapper.entityToResponse(card);
        cardResponse.setOwner(StringMaskedUtils.createOwner(user.getFirstName(),user.getLastName()));
        return cardResponse;
    }
}
