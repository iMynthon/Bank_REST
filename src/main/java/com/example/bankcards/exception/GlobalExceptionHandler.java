package com.example.bankcards.exception;

import com.example.bankcards.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
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
}
