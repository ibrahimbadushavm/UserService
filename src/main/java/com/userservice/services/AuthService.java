package com.userservice.services;

import com.userservice.dtos.SignupResponseDto;
import com.userservice.exceptions.*;

public interface AuthService {

    String Login(String email, String password) throws UseNotFoundException, InvalidPasswordException, SessionLimitExceedException;

    boolean isTokenValid(String token,Long userId);

    SignupResponseDto signUp(String userName, String email, String password) throws DuplicateUserException;

    String logout(Long userId, String token) throws UseNotFoundException, InvalidSessionException;
}
