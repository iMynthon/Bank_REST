package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.request.IsActiveRequest;
import com.example.bankcards.dto.response.AllCardResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/card")
public class CardController {

    private final CardService cardService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/user")
    public AllCardResponse getAllMeCard(@PageableDefault Pageable pageable){
        return cardService.findByCardsToUser(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/isActive")
    public String isActiveCards(@RequestBody IsActiveRequest request){
        return cardService.isActiveRequest(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/create")
    public CardResponse save(@RequestBody CardRequest request){
        return cardService.save(request);
    }
}
