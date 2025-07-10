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
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

import static com.example.bankcards.util.SecurityUtils.userId;
import static com.example.bankcards.util.StringCreatorUtils.*;
import static com.example.bankcards.util.StringUtilsMessage.*;

@Service
@RequiredArgsConstructor
@LogService
@CacheConfig(cacheNames = "redisCacheManager")
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final StringEncryptor stringEncryptor;

    @Transactional(readOnly = true)
    public AllCardResponse findAllCards(Pageable pageable){
        Page<Card> cardPage = cardRepository.findAll(pageable);
        return listEntityToListResponse(cardPage);
    }

    @Transactional(readOnly = true)
    public AllCardResponse findByCardsToUser(Pageable pageable){
         Page<Card> cardPage = cardRepository.findCardByUserId(userId(),pageable);
         return listEntityToListResponse(cardPage);
    }

    @Transactional
    public isActiveResponse isActiveAndBlockRequest(IsActiveRequest request){
        Card card = cardRepository.findByHashCardNumber(request.hashNumberCard())
                        .orElseThrow(()-> new EntityNotFoundException(CARD_ENTITY_NOT_FOUND));
        if(!card.getUser().getId().equals(request.userId())){
            throw new OwnerCardException(OWNER_CARD_EXCEPTION);
        }
        checkIsActive(card.getIsActive(),request.isActive());
        cardRepository.IsActive(card.getHashCardNumber(),request.isActive());
        return new isActiveResponse(maskedNumberCard(stringEncryptor.decrypt(card.getNumberCard())),request.isActive());
    }

    @Transactional
    public String depositMeCard(DepositRequest request){
        cardRepository.addToScore(request.hashNumberCard(),request.amount());
        return String.format("Карта с хэш кодом - %s - успешно пополнена на сумму - %s", request.hashNumberCard(),request.amount());
    }

    @Transactional
    public CardResponse save(CardRequest cardRequest) {
        User user = userRepository.findById(cardRequest.userId())
                .orElseThrow(() -> new EntityNotFoundException(USER_ENTITY_NOT_FOUND));
        Card card = Card.builder()
                .numberCard(generatedNumberCard())
                .paymentSystem(cardRequest.paymentSystem())
                .isActive(false)
                .user(user)
                .score(new BigDecimal(0))
                .build();
        card.setHashCardNumber(hashCard(card.getNumberCard()));
        card.setNumberCard(stringEncryptor.encrypt(card.getNumberCard()));
        cardRepository.save(card);
        return entityToResponse(card);
    }

    @Transactional(readOnly = true)
    public CardProjections searchNumberCardProjections(String numberCard){
        return cardRepository.findNumberCardByUserIdAndHashCardNumber(userId(),numberCard)
                .orElseThrow(()-> new EntityNotFoundException(CARD_ENTITY_NOT_FOUND));
    }

    @Async("AsyncTransfer")
    @Transactional
    public void transfer(String sourceCard, String targetCard, BigDecimal amount){
        cardRepository.addToScore(targetCard,amount);
        cardRepository.subtractFromScore(sourceCard,amount);
    }

    private void checkIsActive(boolean isActive,boolean isActiveRequest){
        if (isActive && isActiveRequest){
            throw new isActiveRequestException(IS_ACTIVE_TRUE_EXCEPTION);
        } else if(!isActive && !isActiveRequest) {
            throw new isActiveRequestException(IS_ACTIVE_FALSE_EXCEPTION);
        }
    }

    private AllCardResponse listEntityToListResponse(Page<Card> cardPage){
        List<CardResponse> responseList = cardPage.getContent().stream().map(this::entityToResponse).toList();
        return new AllCardResponse(new PageImpl<>(responseList,cardPage.getPageable(),cardPage.getTotalElements()));
    }

    private CardResponse entityToResponse(Card card){
        return CardResponse.builder()
                .numberCard(maskedNumberCard(stringEncryptor.decrypt(card.getNumberCard())))
                .hashCardNumber(card.getHashCardNumber())
                .paymentSystem(card.getPaymentSystem())
                .score(card.getScore())
                .owner(createOwner(card.getUser().getFirstName(),card.getUser().getLastName(),card.getUser().getPatronymic()))
                .validityPeriod(createValidityPeriod(card.getValidityPeriodFrom(),card.getValidityPeriodTo()))
                .isActive(card.getIsActive())
                .build();
    }

    private String generatedNumberCard() {
        SecureRandom random = new SecureRandom();
        return String.format("%04d %04d %04d %04d",
                1000 + random.nextInt(9000),
                1000 + random.nextInt(9000),
                1000 + random.nextInt(9000),
                1000 + random.nextInt(9000));
    }
}
