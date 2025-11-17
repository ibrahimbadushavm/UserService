package com.userservice.controllers;

import com.userservice.dtos.ExceptionResponseDto;
import com.userservice.exceptions.DuplicateUserException;
import com.userservice.exceptions.InvalidPasswordException;
import com.userservice.exceptions.SessionLimitExceedException;
import com.userservice.exceptions.UseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({DuplicateUserException.class,SessionLimitExceedException.class,
                            })
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
