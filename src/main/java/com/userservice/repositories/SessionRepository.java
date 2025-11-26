package com.userservice.repositories;

import com.userservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByUserId(Long userId);
    Optional<Session> findByTokenAndUserId(String token, Long userId);
}
