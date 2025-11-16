package com.userservice.models.services;

import com.userservice.repositories.SessionRepository;
import com.userservice.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    public AuthServiceImpl(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public String Login(String username, String password) {
        return "";
    }

    @Override
    public boolean isTokenValid(String token) {
        return false;
    }
}
