package com.userservice.services;

import com.userservice.exceptions.DuplicateUserException;
import com.userservice.exceptions.InvalidPasswordException;
import com.userservice.exceptions.SessionLimitExceedException;
import com.userservice.exceptions.UseNotFoundException;

public interface AuthService {

    String Login(String email, String password) throws UseNotFoundException, InvalidPasswordException, SessionLimitExceedException;

    boolean isTokenValid(String token,Long userId);

    String signUp(String userName,String email, String password) throws DuplicateUserException;
}
