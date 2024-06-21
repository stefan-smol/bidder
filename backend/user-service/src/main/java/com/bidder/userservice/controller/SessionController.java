package com.bidder.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bidder.userservice.dto.SessionResponse;
import com.bidder.userservice.dto.SessionUpdateRequest;
import com.bidder.userservice.model.UserSession;
import com.bidder.userservice.service.UserSessionService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

	@Autowired
	private UserSessionService sessionService;

	@GetMapping("/{username}/{sessionId}")
	public ResponseEntity<SessionResponse> getSession(@PathVariable String username, @PathVariable String sessionId) {
		System.out
				.println("Received request to get session for username: " + username + " and sessionId: " + sessionId);

		UserSession session = sessionService.getSessionByIdAndUsername(sessionId, username);

		if (session != null) {
			System.out.println("Session found. Converting to SessionResponse...");
			SessionResponse response = convertToSessionResponse(session);
			return ResponseEntity.ok(response);
		} else {
			System.out.println("Session not found for username: " + username + " and sessionId: " + sessionId);
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{username}/{sessionId}")
	public ResponseEntity<SessionResponse> updateSession(@PathVariable String username, @PathVariable String sessionId,
			@RequestBody SessionUpdateRequest updateRequest) {
		System.out.println("Received request to update session for username: " + username + " and sessionId: "
				+ sessionId + " with auctionId: " + updateRequest.getAuctionId());

		try {
			UserSession updatedSession = sessionService.updateSession(sessionId, username,
					updateRequest.getAuctionId());
			SessionResponse response = convertToSessionResponse(updatedSession);
			return ResponseEntity.ok(response);
		} catch (EntityNotFoundException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			System.out.println("Error updating session: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private SessionResponse convertToSessionResponse(UserSession session) {
		System.out.println("Converting session to response. Session ID: " + session.getSessionId());
		SessionResponse response = new SessionResponse();
		response.setSessionId(session.getSessionId());
		response.setUsername(session.getUser().getUsername());
		response.setAuctionId(session.getAuctionId());
		return response;
	}
}
