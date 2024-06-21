package com.bidder.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bidder.userservice.model.UserSession;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findBySessionId(String sessionId);
    
    Optional<UserSession> findBySessionIdAndUserUsername(String sessionId, String username);
    
    Optional<UserSession> findByUserIdAndSessionId(Long userId, String sessionId);
}

