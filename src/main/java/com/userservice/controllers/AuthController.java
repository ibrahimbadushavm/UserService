package com.userservice.controllers;

import com.userservice.dtos.*;
import com.userservice.exceptions.*;
import com.userservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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
    public ResponseEntity<SignupResponseDto> signUp(@RequestBody SignupRequestDto signUpRequestDto) throws DuplicateUserException {
        SignupResponseDto response = authService.signUp(signUpRequestDto.getUserName(), signUpRequestDto.getEmail(), signUpRequestDto.getPassword());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(@RequestBody TokenValidRequestDto requestDto) throws InvalidSessionException, UseNotFoundException {
       String message= authService.logout(requestDto.getUserId(), requestDto.getToken());
       LogoutResponseDto logoutResponseDto=new LogoutResponseDto();
       logoutResponseDto.setMessage(message);
       return new ResponseEntity<>(logoutResponseDto,HttpStatus.OK);
    }

}
