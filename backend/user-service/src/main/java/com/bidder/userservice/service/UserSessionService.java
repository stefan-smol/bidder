package com.bidder.userservice.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bidder.userservice.model.User;
import com.bidder.userservice.model.UserSession;
import com.bidder.userservice.repository.UserRepository;
import com.bidder.userservice.repository.UserSessionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;
    
    @Autowired
    private UserRepository userRepository;

    public UserSession createSession(User user) {
        System.out.println("Creating session for user: " + user.getUsername());
        UserSession session = new UserSession();
        session.setSessionId(UUID.randomUUID().toString());
        session.setUser(user);
        UserSession savedSession = userSessionRepository.save(session);
        System.out.println("Session created with ID: " + savedSession.getSessionId());
        return savedSession;
    }
    
    public UserSession updateSession(String sessionId, String username, Long auctionId) {
        System.out.println("Updating session for username: " + username +
                " and session ID: " + sessionId + " with auctionId: " + auctionId);
        
        UserSession session = getSessionByIdAndUsername(sessionId, username);

        if (session.getAuctionId() == null) {
            session.setAuctionId(auctionId);
            return userSessionRepository.save(session);
        } else {
            throw new IllegalStateException("This session already has an auction ID assigned.");
        }
    }

    public boolean validateSession(String sessionId) {
        boolean sessionExists = userSessionRepository.findBySessionId(sessionId).isPresent();
        System.out.println("Validating session with ID: " + sessionId + ". Exists: " + sessionExists);
        return sessionExists;
    }
    
    public UserSession getSessionByIdAndUsername(String sessionId, String username) {
        System.out.println("Retrieving session for username: " + username + " and session ID: " + sessionId);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getId();
            System.out.println("User found with ID: " + userId + ". Retrieving session...");
            return userSessionRepository.findByUserIdAndSessionId(userId, sessionId)
                    .orElseThrow(() -> new EntityNotFoundException("Session not found for the given username and session ID."));
        } else {
            System.out.println("User not found with username: " + username);
            throw new EntityNotFoundException("User not found with username: " + username);
        }
    }
}
