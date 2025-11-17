package com.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String email;
    private String token;
    private Long userId;
}
