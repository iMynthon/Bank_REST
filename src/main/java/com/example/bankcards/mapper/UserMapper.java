package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.UserRequest;
import com.example.bankcards.dto.response.AllUserResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.model.User;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponse entityToResponse(User user);

    @Mapping(target = "registerTime",expression = "java(java.time.LocalDateTime.now())")
    User requestToEntity(UserRequest request);

    default AllUserResponse listEntityToListResponse(Page<User> userPage){
        List<UserResponse> userResponsesList = userPage.getContent().stream().map(this::entityToResponse).toList();
        return new AllUserResponse(new PageImpl<>(userResponsesList,userPage.getPageable(),userPage.getTotalElements()));
    }

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "roles",ignore = true)
    void updateEntity(@MappingTarget User destination, User root);

}
