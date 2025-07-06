package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.AllCardResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.model.Card;
import com.example.bankcards.util.StringMaskedUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper {

    Card requestToEntity(CardRequest request);

    @Mapping(target = "owner",ignore = true)
    @Mapping(target = "numberCard",qualifiedByName = "maskedNumberCard",source = "numberCard")
    CardResponse entityToResponse(Card card);

    default   AllCardResponse listCardToListResponse(Page<Card> cardPage){
        List<CardResponse> cardResponseList = cardPage.getContent().stream().map(this::entityToResponse)
                .toList();
        return new AllCardResponse(new PageImpl<>(cardResponseList,cardPage.getPageable(),cardPage.getTotalElements()));
    }

    @Named("maskedNumberCard")
    default String maskedNumberCard(String numberCard){
        return StringMaskedUtils.maskedNumberCard(numberCard);
    }
}
