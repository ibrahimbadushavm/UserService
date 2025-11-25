package com.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseDto {
    private String email;
    private String message;

    public static SignupResponseDto from(String email, String message) {
        SignupResponseDto dto = new SignupResponseDto();
        dto.setEmail(email);
        dto.setMessage(message);
        return dto;
    }
}
