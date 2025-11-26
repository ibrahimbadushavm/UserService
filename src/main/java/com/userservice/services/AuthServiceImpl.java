package com.userservice.services;

import com.userservice.dtos.SignupResponseDto;
import com.userservice.exceptions.*;
import com.userservice.models.Session;
import com.userservice.models.SessionStatus;
import com.userservice.models.User;
import com.userservice.repositories.SessionRepository;
import com.userservice.repositories.UserRepository;
import com.userservice.utils.RandomStringGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public AuthServiceImpl(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public String Login(String email, String password) throws UseNotFoundException, InvalidPasswordException, SessionLimitExceedException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UseNotFoundException("User Not Found with email " + email);
        }
        User userEntity = user.get();
        if (!bCryptPasswordEncoder.matches(password,userEntity.getPassword())) {
            throw new InvalidPasswordException("Invalid Password");
        }
        List<Session> sessions = sessionRepository.findByUserId(userEntity.getId());
        Long count = sessions.stream()
                .filter(session -> session.getStatus() == SessionStatus.ACTIVE)
                .count();
        if (count > 1) {
            throw new SessionLimitExceedException("Session Limit Exceeded");
        }
        Map<String,Object> userDetails = Map.of(
                "userId", userEntity.getId(),
                "email", userEntity.getEmail(),
                "roles",userEntity.getRoles()
        );
        String jjwtToken= Jwts.builder()
                .setClaims(userDetails)
                .signWith(SECRET_KEY,SignatureAlgorithm.HS256)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+3600000))
                .compact();
        Session session = new Session();
        session.setUser(userEntity);
        session.setStatus(SessionStatus.ACTIVE);
        session.setToken(jjwtToken);
        session = sessionRepository.save(session);
        return session.getToken();
    }

    @Override
    public boolean isTokenValid(String token, Long userId) {
        List<Session> sessions = sessionRepository.findByUserId(userId);
        for (Session session : sessions) {
            if (session.getToken().equals(token) && session.getStatus() == SessionStatus.ACTIVE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SignupResponseDto signUp(String userName, String email, String password) throws DuplicateUserException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new DuplicateUserException("User already exists with email " + email);
        }
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if (userOptional.isPresent()) {
            throw new DuplicateUserException("User already exists with userName " + userName);
        }

        User userEntity = new User();
        userEntity.setEmail(email);
        userEntity.setUserName(userName);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        userEntity.setPassword(encryptedPassword);
        userEntity = userRepository.save(userEntity);
        return SignupResponseDto.from(userEntity.getEmail(), "User registered successfully");
    }

    @Override
    public String logout(Long userId, String token) throws UseNotFoundException, InvalidSessionException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new UseNotFoundException("User Not Found with id " + userId);
        }
        Optional<Session> sessionOptional =sessionRepository.findByTokenAndUserId(token, userId);
        if(sessionOptional.isEmpty()){
            throw new InvalidSessionException("Invalid Session");
        }
        Session session = sessionOptional.get();
        session.setStatus(SessionStatus.LOGGED_OUT);
        session = sessionRepository.save(session);
        return "Logged out successfully";
    }
}
