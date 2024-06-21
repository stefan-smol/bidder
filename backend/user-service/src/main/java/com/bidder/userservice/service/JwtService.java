package com.bidder.userservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	public static final long EXPIRATION = 1000 * 60 * 30; // 30 minutes;
	public static final long REFRESH_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 1 week

	/*
	 * TO DO: add environment variables for final build
	 * 
	 * @Value("${application.security.jwt.secret-key}") private String secretKey;
	 * 
	 * @Value("${application.security.jwt.expiration}") private long jwtExpiration;
	 * 
	 * @Value("${application.security.jwt.refresh-token.expiration}") private long
	 * refreshExpiration;
	 */

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();

		String role = userDetails.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority())
				.collect(Collectors.joining(","));
		claims.put("role", role);

		return createToken(claims, userDetails.getUsername(), EXPIRATION);
	}
	
	public String generateTokenWithSessionId(UserDetails userDetails, String sessionId) {
		Map<String, Object> claims = new HashMap<>();

		String role = userDetails.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority())
				.collect(Collectors.joining(","));
		claims.put("role", role);
		claims.put("sessionId", sessionId);

		return createToken(claims, userDetails.getUsername(), EXPIRATION);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		return createToken(new HashMap<>(), userDetails.getUsername(), REFRESH_EXPIRATION);
	}
	
	public String generateRefreshTokenWithSessionId(UserDetails userDetails, String sessionId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("sessionId", sessionId);
		
		return createToken(claims, userDetails.getUsername(), REFRESH_EXPIRATION);
	}

	private String createToken(Map<String, Object> claims, String username, long expiration) {
		return Jwts.builder().subject(username).claims(claims)
				.expiration(new Date(System.currentTimeMillis() + expiration))
				.issuedAt(new Date(System.currentTimeMillis())).signWith(getSignKey()).compact();
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public String extractSessionId(String token) {
        return extractClaim(token, claims -> claims.get("sessionId", String.class));
    }

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith((SecretKey) getSignKey()).build().parseSignedClaims(token).getPayload();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
}