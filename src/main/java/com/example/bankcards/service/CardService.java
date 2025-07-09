package com.example.bankcards.service;
import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.request.IsActiveRequest;
import com.example.bankcards.dto.response.AllCardResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.model.Card;
import com.example.bankcards.model.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.projections.CardProjections;
import com.example.bankcards.util.SecurityUtils;
import com.example.bankcards.util.StringMaskedUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public AllCardResponse findAllCards(Pageable pageable){
        Page<Card> cardPage = cardRepository.findAll(pageable);
        return listEntityToListResponse(cardPage);
    }

    @Transactional(readOnly = true)
    public AllCardResponse findByCardsToUser(Pageable pageable){
         Page<Card> cardPage = cardRepository.findCardByUserId(SecurityUtils.userId(),pageable);
         return listEntityToListResponse(cardPage);
    }

    @Transactional(readOnly = true)
    public CardResponse findByNumberCard(String numberCard){
        return entityToResponse(cardRepository.findByNumberCard(numberCard)
                .orElseThrow(()-> new EntityNotFoundException("Карта с таким номер не зарегистрирована")));
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
                .numberCard(cardRequest.numberCard())
                .paymentSystem(cardRequest.paymentSystem())
                .isActive(false)
                .user(user)
                .build());
        return entityToResponse(card);
    }

    private AllCardResponse listEntityToListResponse(Page<Card> cardPage){
        List<CardResponse> responseList = cardPage.getContent().stream().map(this::entityToResponse).toList();
        return new AllCardResponse(new PageImpl<>(responseList,cardPage.getPageable(),cardPage.getTotalElements()));
    }

    private CardResponse entityToResponse(Card card){
        return CardResponse.builder()
                .numberCard(StringMaskedUtils.maskedNumberCard(card.getNumberCard()))
                .paymentSystem(card.getPaymentSystem())
                .owner(StringMaskedUtils.createOwner(card.getUser().getFirstName(),card.getUser().getLastName(),card.getUser().getPatronymic()))
                .validityPeriod(StringMaskedUtils.createValidityPeriod(card.getValidityPeriodFrom(),card.getValidityPeriodTo()))
                .build();
    }
}
