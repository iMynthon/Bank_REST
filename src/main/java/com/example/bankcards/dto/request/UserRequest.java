package com.example.bankcards.dto.request;

import com.example.bankcards.model.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRequest(
        @NotBlank(message = "В запросе не указано имя - firstName")
        String firstName,
        @NotBlank(message = "В запросе не указана фамилия - lastName")
        String lastName,
        String patronymic,
        @NotBlank(message = "В запросе не указан номер телефона - phoneNumber")
        @Pattern(message = "Формат введенного вами номера не валидный", regexp = "^\\+\\d{1,3}\\d{4,15}$")
        String phoneNumber,
        @NotBlank(message = "В запросе не задан пароль - password")
        @Size(min = 5, message = "Пароль не может быть короче {min} символов")
        String password,
        @NotNull(message = "В запросе не задан роль пользователя")
        RoleType role
) {

}
