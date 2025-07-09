package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardTransferRequest;
import com.example.bankcards.dto.response.AllCardTransferResponse;
import com.example.bankcards.dto.response.CardTransferResponse;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.model.CardTransfer;
import com.example.bankcards.model.StatusTransfer;
import com.example.bankcards.repository.CardTransferRepository;
import com.example.bankcards.repository.projections.CardProjections;
import com.example.bankcards.util.SecurityUtils;
import com.example.bankcards.util.StringMaskedUtils;
import com.example.bankcards.util.StringUtilsMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class CardTransferService {

    private final CardTransferRepository transferRepository;
    private final CardService cardService;

    @Transactional(readOnly = true)
    public AllCardTransferResponse findMeTransfer(){
        return new AllCardTransferResponse(transferRepository.findByUserId(SecurityUtils.userId())
                .stream().map(this::entityToResponse).toList());
    }

    @Transactional
    public CardTransferResponse transferMeCards(CardTransferRequest request){
        CardProjections sourceCard = cardService.searchNumberCardProjections(request.sourceCard());
        CardProjections targetCard = cardService.searchNumberCardProjections(request.targetCard());
        if(sourceCard.getScore().compareTo(request.amount()) < 0){
            createTransferUnsuccessfully(sourceCard.getNumberCard(),targetCard.getNumberCard(),request.amount(),StringUtilsMessage.INSUFFICIENT_FUNDS);
            throw new InsufficientFundsException(StringUtilsMessage.INSUFFICIENT_FUNDS);
        }
        cardService.transfer(sourceCard.getNumberCard(),targetCard.getNumberCard(),request.amount());
        CardTransfer cardTransfer = createTransferSuccessfully(sourceCard.getNumberCard(),targetCard.getNumberCard(),request.amount());
        return entityToResponse(transferRepository.save(cardTransfer));
    }

    private CardTransferResponse entityToResponse(CardTransfer cardTransfer){
        return CardTransferResponse.builder()
                .sourceCard(StringMaskedUtils.maskedNumberCard(cardTransfer.getSourceCard()))
                .targetCard(StringMaskedUtils.maskedNumberCard(cardTransfer.getTargetCard()))
                .amount(cardTransfer.getAmount())
                .statusTransfer(StatusTransfer.SUCCESSFULLY)
                .transferTime(cardTransfer.getTransferTime())
                .build();
    }

    private void createTransferUnsuccessfully(String sourceCard, String targetCard, BigDecimal amount,String errorMessage){
        transferRepository.save(CardTransfer.builder()
                        .userId(SecurityUtils.userId())
                .statusTransfer(StatusTransfer.UNSUCCESSFULLY)
                .targetCard(targetCard)
                .sourceCard(sourceCard)
                .amount(amount)
                .errorMessage(errorMessage)
                .transferTime(LocalDateTime.now())
                .build());
    }

    private CardTransfer createTransferSuccessfully(String sourceCard, String targetCard, BigDecimal amount){
        return CardTransfer.builder()
                .userId(SecurityUtils.userId())
                .statusTransfer(StatusTransfer.SUCCESSFULLY)
                .targetCard(targetCard)
                .sourceCard(sourceCard)
                .amount(amount)
                .transferTime(LocalDateTime.now())
                .build();
    }
}
