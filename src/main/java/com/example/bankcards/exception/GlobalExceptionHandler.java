package com.example.bankcards.exception;

import com.example.bankcards.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse catchEntityNotFoundException(EntityNotFoundException efe){
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), efe.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientFundsException.class)
    public ErrorResponse catchInsufficientFundsException(InsufficientFundsException ife){
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),ife.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(isActiveRequestException.class)
    public ErrorResponse catchIsActiveRequestException(isActiveRequestException iar){
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),iar.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse catchValidationException(MethodArgumentNotValidException ve){
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),"Ошибка валидации, неправильный ввод или некорректные данные - " + ve.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse("Ошибка валидации"));
    }
}
