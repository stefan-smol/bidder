package com.bidder.auctionprocessingservice.service;

import com.bidder.auctionprocessingservice.dto.AuctionResponse;
import com.bidder.auctionprocessingservice.dto.SessionResponse;
import com.bidder.auctionprocessingservice.dto.ValidationResult;
import com.bidder.auctionprocessingservice.model.Bid;
import com.bidder.auctionprocessingservice.repository.BidRepository;
import com.bidder.auctionprocessingservice.service.MessagePublisher.AuctionType;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class BidService {

	private final RestTemplate restTemplate;

	@Autowired
	private BidRepository bidRepository;

	@Autowired
	private MessagePublisher messagePublisher;

	@Autowired
	public BidService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}
	
    public AuctionResponse getAuctionDetails(Long auctionId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<AuctionResponse> response = restTemplate.exchange(
            "http://api-gateway:8080/api/v1/catalogue/auctions/" + auctionId, 
            HttpMethod.GET, 
            entity, 
            AuctionResponse.class
        );

        return response.getBody();
    }

    public SessionResponse getUserSession(String username, String sessionId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token); 
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<SessionResponse> sessionResponse = restTemplate.exchange(
            "http://api-gateway:8080/api/v1/sessions/" + username + "/" + sessionId, 
            HttpMethod.GET, 
            entity, 
            SessionResponse.class
        );

        return sessionResponse.getBody();
    }
    
    public void updateUserSession(String username, String sessionId, Long auctionId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token); 
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Long> updateBody = new HashMap<>();
        updateBody.put("auctionId", auctionId);

        HttpEntity<Map<String, Long>> entity = new HttpEntity<>(updateBody, headers);

        restTemplate.exchange(
            "http://api-gateway:8080/api/v1/sessions/" + username + "/" + sessionId, 
            HttpMethod.PUT, 
            entity, 
            Void.class
        );
    }


    public ValidationResult validateBid(Bid bid, String token) {
        AuctionResponse auction = getAuctionDetails(bid.getAuctionId(), token);
        SessionResponse session = getUserSession(bid.getUsername(), bid.getSessionId(), token);
        
        if (session.getAuctionId() == null) {
            updateUserSession(bid.getUsername(), bid.getSessionId(), bid.getAuctionId(), token);
        }

        if (session.getAuctionId() != null && !session.getAuctionId().equals(bid.getAuctionId())) {
            return ValidationResult.fail("Cannot bid on multiple items in the same session.");
        }

        if (auction == null) {
            return ValidationResult.fail("Auction with ID " + bid.getAuctionId() + " not found.");
        }

        if (auction.getStatus() != AuctionResponse.AuctionStatus.RUNNING) {
            return ValidationResult.fail("Auction with ID " + bid.getAuctionId() + " is not running. Current status: " + auction.getStatus());
        }

        if (bid.getUsername() == null || bid.getUsername().isEmpty()) {
            return ValidationResult.fail("Bid username is null or empty.");
        }

        Double currentHighestBid = auction.getCurrentHighestBid() != null ? auction.getCurrentHighestBid() : 0.0;

        if (bid.getBidAmount() == null || bid.getBidAmount() <= 0) {
            return ValidationResult.fail("Bid amount is null or not greater than 0.");
        }

        if (bid.getBidAmount() <= currentHighestBid) {
            return ValidationResult.fail("Bid amount " + bid.getBidAmount() + " is not higher than current highest bid " + currentHighestBid + ".");
        }

        if (bid.getBidTime() == null) {
            return ValidationResult.fail("Bid time is null.");
        }

        if (bid.getBidTime().before(auction.getStartTime()) || bid.getBidTime().after(auction.getExpiryTime())) {
            return ValidationResult.fail("Bid time " + bid.getBidTime() + " is outside the auction's start time " + auction.getStartTime() + " and expiry time " + auction.getExpiryTime() + ".");
        }

        return ValidationResult.ok();
    }


	public void placeBid(Bid bid, String token) {
		if (validateBid(bid, token).isValid()) {
			bidRepository.save(bid);
			messagePublisher.publish(bid, AuctionType.FORWARD);
		} else {
			throw new IllegalArgumentException("Bid is not valid");
		}
	}
}
