package com.example.bankcards.controller;

import com.example.bankcards.aop.LogController;
import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.request.CardTransferRequest;
import com.example.bankcards.dto.request.DepositRequest;
import com.example.bankcards.dto.request.IsActiveRequest;
import com.example.bankcards.dto.response.*;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.CardTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/card")
@LogController
@Validated
public class CardController {

    private final CardService cardService;
    private final CardTransferService cardTransferService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public AllCardResponse getAllCards(@PageableDefault Pageable pageable){
        return cardService.findAllCards(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public AllCardResponse getAllMeCard(@PageableDefault Pageable pageable){
        return cardService.findByCardsToUser(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public isActiveResponse isActiveAndBlockCards(@RequestBody @Valid IsActiveRequest request){
        return cardService.isActiveAndBlockRequest(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CardResponse save(@RequestBody @Valid CardRequest request){
        return cardService.save(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/me/deposit")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String depositMeCard(@RequestBody @Valid DepositRequest request){
        return cardService.depositMeCard(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CardTransferResponse transferMeCards(@RequestBody @Valid CardTransferRequest request){
        return cardTransferService.transferMeCards(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me/transfers")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public AllCardTransferResponse allMeCardsTransfer(){
        return cardTransferService.findMeTransfer();
    }
}
