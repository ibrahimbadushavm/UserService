package com.userservice.controllers;

import com.userservice.dtos.ExceptionResponseDto;
import com.userservice.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({DuplicateUserException.class,SessionLimitExceedException.class
                            , InvalidSessionException.class})
    public ResponseEntity<ExceptionResponseDto> handleException(Exception e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto();
        exceptionResponseDto.setMessage(e.getMessage());
        return new ResponseEntity<ExceptionResponseDto>(exceptionResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidPasswordException.class, UseNotFoundException.class})
    public ResponseEntity<ExceptionResponseDto> handleExceptionUnAuth(Exception e) {
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto();
        exceptionResponseDto.setMessage(e.getMessage());
        return new ResponseEntity<ExceptionResponseDto>(exceptionResponseDto, HttpStatus.UNAUTHORIZED);
    }
}
