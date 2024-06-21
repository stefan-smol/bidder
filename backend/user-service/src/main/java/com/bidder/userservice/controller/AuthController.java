package com.bidder.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bidder.userservice.dto.AuthRequest;
import com.bidder.userservice.dto.AuthResponse;
import com.bidder.userservice.dto.PasswordResetRequest;
import com.bidder.userservice.dto.RegisterRequest;
import com.bidder.userservice.service.AuthService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
        @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
        @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    
    @PostMapping("/resetPassword")
    public ResponseEntity<AuthResponse> resetPassword(
        @RequestBody PasswordResetRequest request
    ) {
        return ResponseEntity.ok(service.resetPassword(request));
    }

    @PostMapping("/refreshToken")
    public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
    
    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bearer token not found in request header");
        }
        
        String token = authorizationHeader.substring(7);

        try {
            if (service.validateToken(token)) {
                return ResponseEntity.ok().body("Token is valid");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during token validation");
        }
    }

}
