package com.example.bankcards.service;
import com.example.bankcards.aop.LogService;
import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.request.DepositRequest;
import com.example.bankcards.dto.request.IsActiveRequest;
import com.example.bankcards.dto.response.AllCardResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.dto.response.isActiveResponse;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.exception.OwnerCardException;
import com.example.bankcards.exception.isActiveRequestException;
import com.example.bankcards.model.Card;
import com.example.bankcards.model.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.repository.projections.CardProjections;
import com.example.bankcards.util.SecurityUtils;
import com.example.bankcards.util.StringMaskedUtils;
import com.example.bankcards.util.StringUtilsMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@LogService
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public isActiveResponse isActiveAndBlockRequest(IsActiveRequest request){
        Card card = cardRepository.findByNumberCard(request.numberCard())
                        .orElseThrow(()-> new EntityNotFoundException(StringUtilsMessage.CARD_ENTITY_NOT_FOUND));
        if(!card.getUser().getId().equals(request.userId())){
            throw new OwnerCardException(StringUtilsMessage.OWNER_CARD_EXCEPTION);
        }
        checkIsActive(card.getIsActive(),request.isActive());
        cardRepository.IsActive(card.getNumberCard(),request.isActive());
        return new isActiveResponse(request.numberCard(),request.isActive());
    }

    @Transactional
    public String depositMeCard(DepositRequest request){
        cardRepository.addToScore(request.numberCard(),request.score());
        return String.format("Карта - %s - успешно пополнена неа сумму - %s",StringMaskedUtils.maskedNumberCard(request.numberCard()),request.score());
    }

    @Transactional
    public CardResponse save(CardRequest cardRequest) {
        User user = userRepository.findById(cardRequest.userId())
                .orElseThrow(() -> new EntityNotFoundException(StringUtilsMessage.USER_ENTITY_NOT_FOUND));
        Card card = cardRepository.save(Card.builder()
                .numberCard(cardRequest.numberCard())
                .paymentSystem(cardRequest.paymentSystem())
                .isActive(false)
                .user(user)
                .score(new BigDecimal(0))
                .build());
        return entityToResponse(card);
    }

    @Transactional(readOnly = true)
    public CardProjections searchNumberCardProjections(String numberCard){
        return cardRepository.findNumberCardByUserIdAndNumberCard(SecurityUtils.userId(),numberCard)
                .orElseThrow(()-> new EntityNotFoundException(StringUtilsMessage.SOURCE_CARD_EXCEPTION));
    }

    @Async("AsyncTransfer")
    @Transactional
    public void transfer(String sourceCard, String targetCard, BigDecimal amount){
        cardRepository.addToScore(targetCard,amount);
        cardRepository.subtractFromScore(sourceCard,amount);
    }

    private void checkIsActive(boolean isActive,boolean isActiveRequest){
        if (isActive && isActiveRequest){
            throw new isActiveRequestException(StringUtilsMessage.isActiveTrueException);
        } else if(!isActive && !isActiveRequest) {
            throw new isActiveRequestException(StringUtilsMessage.isActiveFalseException);
        }
    }

    private AllCardResponse listEntityToListResponse(Page<Card> cardPage){
        List<CardResponse> responseList = cardPage.getContent().stream().map(this::entityToResponse).toList();
        return new AllCardResponse(new PageImpl<>(responseList,cardPage.getPageable(),cardPage.getTotalElements()));
    }

    private CardResponse entityToResponse(Card card){
        return CardResponse.builder()
                .numberCard(StringMaskedUtils.maskedNumberCard(card.getNumberCard()))
                .paymentSystem(card.getPaymentSystem())
                .score(card.getScore())
                .owner(StringMaskedUtils.createOwner(card.getUser().getFirstName(),card.getUser().getLastName(),card.getUser().getPatronymic()))
                .validityPeriod(StringMaskedUtils.createValidityPeriod(card.getValidityPeriodFrom(),card.getValidityPeriodTo()))
                .isActive(card.getIsActive())
                .build();
    }
}
