package com.userservice.models.services;

public interface AuthService {

    String Login(String username, String password);

    boolean isTokenValid(String token);
}
