package com.bidder.auctionprocessingservice.service;

import com.bidder.auctionprocessingservice.dto.AuctionResponse;
import com.bidder.auctionprocessingservice.dto.SessionResponse;
import com.bidder.auctionprocessingservice.dto.ValidationResult;
import com.bidder.auctionprocessingservice.model.Purchase;
import com.bidder.auctionprocessingservice.repository.PurchaseRepository;
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
public class PurchaseService {

	private final RestTemplate restTemplate;

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private MessagePublisher messagePublisher;

	@Autowired
	public PurchaseService(RestTemplateBuilder restTemplateBuilder) {
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

        ResponseEntity<SessionResponse> response = restTemplate.exchange(
            "http://api-gateway:8080/api/v1/sessions/" + username + "/" + sessionId, 
            HttpMethod.GET, 
            entity, 
            SessionResponse.class
        );

        return response.getBody();
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

    public ValidationResult validatePurchase(Purchase purchase, String token) {
        AuctionResponse auction = getAuctionDetails(purchase.getAuctionId(), token);
        SessionResponse session = getUserSession(purchase.getUsername(), purchase.getSessionId(), token);
        
        if (session.getAuctionId() == null) {
            updateUserSession(purchase.getUsername(), purchase.getSessionId(), purchase.getAuctionId(), token);
        }
        
        if (session.getAuctionId() == null) {
            session.setAuctionId(purchase.getAuctionId());
        } else if (!session.getAuctionId().equals(purchase.getAuctionId())) {
            return new ValidationResult(false, "Cannot purchase multiple items in the same session.");
        }
        
        if (auction == null) {
            return new ValidationResult(false, "Auction is null for auction ID: " + purchase.getAuctionId());
        }

        if (auction.getStatus() != AuctionResponse.AuctionStatus.RUNNING) {
            return new ValidationResult(false, "Auction is not running. Current status: " + auction.getStatus());
        }

        if (purchase.getUsername() == null || purchase.getUsername().isEmpty()) {
            return new ValidationResult(false, "Purchase username is null or empty");
        }

        Double purchasePrice = auction.getPurchasePrice();
        if (purchase.getPurchaseAmount() == null) {
            return new ValidationResult(false, "Purchase amount is null");
        }

        if (purchase.getPurchaseAmount() <= 0) {
            return new ValidationResult(false, "Purchase amount is less than or equal to 0. Purchase amount: " + purchase.getPurchaseAmount());
        }

        if (purchasePrice != null && !purchase.getPurchaseAmount().equals(purchasePrice)) {
            return new ValidationResult(false, "Purchase amount does not equal auction purchase price. Purchase amount: " + purchase.getPurchaseAmount() + ", Auction purchase price: " + purchasePrice);
        }

        if (purchase.getPurchaseTime() == null) {
            return new ValidationResult(false, "Purchase time is null");
        }

        if (purchase.getPurchaseTime().before(auction.getStartTime())) {
            return new ValidationResult(false, "Purchase time is before auction start time. Purchase time: " + purchase.getPurchaseTime() + ", Auction start time: " + auction.getStartTime());
        }

        return new ValidationResult(true, null);
    }

	public void placePurchase(Purchase purchase, String token) {
		if (validatePurchase(purchase, token).isValid()) {
			purchaseRepository.save(purchase);
			messagePublisher.publish(purchase, AuctionType.DUTCH);
		} else {
			throw new IllegalArgumentException("Bid is not valid");
		}
	}
}


