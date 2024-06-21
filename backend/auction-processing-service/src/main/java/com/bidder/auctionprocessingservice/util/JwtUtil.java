package com.bidder.auctionprocessingservice.util;

import java.security.Key;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	
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

}
