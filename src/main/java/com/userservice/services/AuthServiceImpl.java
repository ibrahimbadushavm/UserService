package com.userservice.services;

import com.userservice.exceptions.DuplicateUserException;
import com.userservice.exceptions.InvalidPasswordException;
import com.userservice.exceptions.SessionLimitExceedException;
import com.userservice.exceptions.UseNotFoundException;
import com.userservice.models.Session;
import com.userservice.models.SessionStatus;
import com.userservice.models.User;
import com.userservice.repositories.SessionRepository;
import com.userservice.repositories.UserRepository;
import com.userservice.utils.RandomStringGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthServiceImpl(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
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
        Session session = new Session();
        session.setUser(userEntity);
        session.setStatus(SessionStatus.ACTIVE);
        session.setToken(RandomStringGenerator.generateRandomStringWithLength(20));
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
    public String signUp(String userName, String email, String password) throws DuplicateUserException {
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
        return userName;
    }
}
