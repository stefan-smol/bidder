package com.bidder.userservice.service;

import com.bidder.userservice.service.JwtService;
import com.bidder.userservice.dto.AuthRequest;
import com.bidder.userservice.dto.AuthResponse;
import com.bidder.userservice.dto.PasswordResetRequest;
import com.bidder.userservice.dto.RegisterRequest;
import com.bidder.userservice.model.ShippingAddress;
import com.bidder.userservice.model.User;
import com.bidder.userservice.model.UserSession;
import com.bidder.userservice.repository.ShippingAddressRepository;
import com.bidder.userservice.repository.UserRepository;
import com.bidder.userservice.repository.UserSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthService {
	private final UserRepository userRepository;
	private final ShippingAddressRepository shippingAddressRepository;
	private final UserSessionRepository userSessionRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final UserService userService;
	private final UserSessionService sessionService;
	private final AuthenticationManager authenticationManager;

	public AuthService(UserRepository userRepository, ShippingAddressRepository shippingAddressRepository, UserSessionRepository userSessionRepository,
			PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, UserService userService, UserSessionService sessionService) {
		this.userRepository = userRepository;
		this.shippingAddressRepository = shippingAddressRepository;
		this.userSessionRepository = userSessionRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.userService = userService;
		this.sessionService = sessionService;
		this.authenticationManager = authenticationManager;
	}

	public AuthResponse register(RegisterRequest request) {
		var user = User
				.builder()
				.username(request.getUsername())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.role(request.getRole())
				.build();
		
		var shippingAddress = new ShippingAddress.Builder()
		        .country(request.getCountry())
		        .city(request.getCity())
		        .postalCode(request.getPostalCode())
		        .streetAddress(request.getStreetAddress())
		        .streetNumber(request.getStreetNumber())
		        .user(user)
		        .build();

		
		var savedUser = userRepository.save(user);
		var savedShippingAddress = shippingAddressRepository.save(shippingAddress);
		
		savedUser.setShippingAddress(savedShippingAddress);
	    userRepository.save(savedUser);
		
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		
		return AuthResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	public AuthResponse authenticate(AuthRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		
		var user = userRepository
				.findByUsername(request.getUsername())
				.orElseThrow();
		
	    UserSession newSession = sessionService.createSession(user);
	    
		var jwtToken = jwtService.generateTokenWithSessionId(user, newSession.getSessionId());
		var refreshToken = jwtService.generateRefreshTokenWithSessionId(user, newSession.getSessionId());
		
		return AuthResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}
	
	public AuthResponse resetPassword(PasswordResetRequest request) {
		var user = userRepository
				.findByUsername(request.getUsername())
				.orElseThrow();

	    user.setPassword(passwordEncoder.encode(request.getPassword()));
	    userRepository.save(user);

	    var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		
		return AuthResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String username;
		final String sessionId;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		username = jwtService.extractUsername(refreshToken);
		sessionId = jwtService.extractSessionId(refreshToken);
		if (username != null) {
			var user = this.userRepository.findByUsername(username).orElseThrow();
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateTokenWithSessionId(user, sessionId);
				var authResponse = AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
						.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}
	
	public boolean validateToken(String token) {
	    try {
	        System.out.println("Validating token: " + token);
	        String username = jwtService.extractUsername(token);
	        System.out.println("Extracted username: " + username);
	        UserDetails userDetails = userService.loadUserByUsername(username);
	        System.out.println("Loaded userDetails for: " + username);
	        boolean isValid = jwtService.isTokenValid(token, userDetails);
	        System.out.println("Is token valid: " + isValid);
	        return isValid;
	    } catch (Exception e) {
	        System.out.println("Exception in validateToken: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}

	
}