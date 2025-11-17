package com.userservice.controllers;

import com.userservice.dtos.LoginRequestDto;
import com.userservice.dtos.LoginResponseDto;
import com.userservice.dtos.SignupRequestDto;
import com.userservice.dtos.TokenValidRequestDto;
import com.userservice.exceptions.DuplicateUserException;
import com.userservice.exceptions.InvalidPasswordException;
import com.userservice.exceptions.SessionLimitExceedException;
import com.userservice.exceptions.UseNotFoundException;
import com.userservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) throws UseNotFoundException, SessionLimitExceedException, InvalidPasswordException {
        String token = authService.Login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setEmail(loginRequestDto.getEmail());
        loginResponseDto.setToken(token);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }

    @PostMapping("/validatetoken")
    public ResponseEntity<Boolean> validateToken(@RequestBody TokenValidRequestDto requestDto) {
        boolean isValid = authService.isTokenValid(requestDto.getToken(), requestDto.getUserId());
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignupRequestDto signUpRequestDto) throws DuplicateUserException {
        String response = authService.signUp(signUpRequestDto.getUserName(), signUpRequestDto.getEmail(), signUpRequestDto.getPassword());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
