package com.example.bankcards.service;

import com.example.bankcards.aop.LogService;
import com.example.bankcards.dto.request.CardTransferRequest;
import com.example.bankcards.dto.response.AllCardTransferResponse;
import com.example.bankcards.dto.response.CardTransferResponse;
import com.example.bankcards.exception.TransferException;
import com.example.bankcards.model.CardTransfer;
import com.example.bankcards.model.StatusTransfer;
import com.example.bankcards.repository.CardTransferRepository;
import com.example.bankcards.repository.projections.CardProjections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.example.bankcards.util.SecurityUtils.userId;
import static com.example.bankcards.util.StringUtilsMessage.ACTIVE_TRANSFER_EXCEPTION;
import static com.example.bankcards.util.StringUtilsMessage.INSUFFICIENT_FUNDS;


@Slf4j
@Service
@RequiredArgsConstructor
@LogService
public class CardTransferService {

    private final CardTransferRepository transferRepository;
    private final CardService cardService;

    @Transactional(readOnly = true)
    public AllCardTransferResponse findMeTransfer(){
        return new AllCardTransferResponse(transferRepository.findByUserId(userId())
                .stream().map(this::entityToResponse).toList());
    }

    @Transactional
    public CardTransferResponse transferMeCards(CardTransferRequest request){
        CardProjections sourceCard = cardService.searchNumberCardProjections(request.hashSourceCard());
        CardProjections targetCard = cardService.searchNumberCardProjections(request.hashTargetCard());
        validateTransfer(sourceCard,targetCard,request.amount());
        cardService.transfer(sourceCard.getHashCardNumber(),targetCard.getHashCardNumber(),request.amount());
        CardTransfer cardTransfer = createTransferSuccessfully(sourceCard.getHashCardNumber(),targetCard.getHashCardNumber(),request.amount());
        return entityToResponse(transferRepository.save(cardTransfer));
    }

    private void validateTransfer(CardProjections sourceCard,CardProjections targetCard,BigDecimal requestAmount){
         if(!sourceCard.getIsActive() || !targetCard.getIsActive()){
             createTransferUnsuccessfully(sourceCard.getHashCardNumber(),targetCard.getHashCardNumber(),requestAmount,ACTIVE_TRANSFER_EXCEPTION);
             if(!sourceCard.getIsActive()){
                 throw new TransferException(String.format("Карта с хэш номером не активирована, ошибка перевода - %s",sourceCard.getHashCardNumber()));
             } else {
                 throw new TransferException(String.format("Карта с хэш номером не активирована, ошибка перевода - %s",targetCard.getHashCardNumber()));
             }
         } else if(sourceCard.getScore().compareTo(requestAmount) < 0){
            createTransferUnsuccessfully(sourceCard.getHashCardNumber(),targetCard.getHashCardNumber(),requestAmount,INSUFFICIENT_FUNDS);
            throw new TransferException(INSUFFICIENT_FUNDS);
        }
    }

    private CardTransferResponse entityToResponse(CardTransfer cardTransfer){
        return CardTransferResponse.builder()
                .hashSourceCard(cardTransfer.getSourceHashCard())
                .hashTargetCard(cardTransfer.getTargetHashCard())
                .amount(cardTransfer.getAmount())
                .statusTransfer(StatusTransfer.SUCCESSFULLY)
                .transferTime(cardTransfer.getTransferTime())
                .build();
    }

    private void createTransferUnsuccessfully(String hashSourceCard, String hashTargetCard, BigDecimal amount,String errorMessage){
        transferRepository.save(CardTransfer.builder()
                        .userId(userId())
                .statusTransfer(StatusTransfer.UNSUCCESSFULLY)
                .targetHashCard(hashTargetCard)
                .sourceHashCard(hashSourceCard)
                .amount(amount)
                .errorMessage(errorMessage)
                .transferTime(LocalDateTime.now())
                .build());
    }

    private CardTransfer createTransferSuccessfully(String hashSourceCard, String hashTargetCard, BigDecimal amount){
        return CardTransfer.builder()
                .userId(userId())
                .statusTransfer(StatusTransfer.SUCCESSFULLY)
                .sourceHashCard(hashSourceCard)
                .targetHashCard(hashTargetCard)
                .amount(amount)
                .transferTime(LocalDateTime.now())
                .build();
    }
}
