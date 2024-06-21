package com.bidder.auctionprocessingservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bidder.auctionprocessingservice.util.JwtUtil;
import com.bidder.auctionprocessingservice.dto.ValidationResult;
import com.bidder.auctionprocessingservice.model.Bid;
import com.bidder.auctionprocessingservice.service.BidService;
import com.bidder.auctionprocessingservice.model.Purchase;
import com.bidder.auctionprocessingservice.service.PurchaseService;

@RestController
@RequestMapping("/api/v1/process")
public class AuctionProcessingController {

	@Autowired
	private BidService bidService;

	@Autowired
	private PurchaseService purchaseService;

	@Autowired
	private JwtUtil JwtUtil;

	@PostMapping("/bid")
	public ResponseEntity<Object> placeBid(@RequestBody Bid bid,
			@RequestHeader(name = "Authorization", required = false) String token) {

		if (token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
		}

		try {
			String jwtToken = token.substring(7);
			String bidderUsername = JwtUtil.extractUsername(jwtToken);
			String bidSession = JwtUtil.extractSessionId(jwtToken);

			bid.setUsername(bidderUsername);
			bid.setSessionId(bidSession);
			
			ValidationResult validationResult = bidService.validateBid(bid, token);
			
			System.out.println("Result message: " + validationResult.getMessage());
			
			if (validationResult.isValid()) {
	            bidService.placeBid(bid, token);
	            return ResponseEntity.ok(Map.of("message", "Bid placed successfully."));
	        } else {
	            return ResponseEntity.badRequest().body(Map.of("error", validationResult.getMessage()));
	        }
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/purchase")
	public ResponseEntity<Object> placePurchase(@RequestBody Purchase purchase,
			@RequestHeader(name = "Authorization", required = false) String token) {

		if (token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
		}

		try {
			String jwtToken = token.substring(7);
			String purchaserUsername = JwtUtil.extractUsername(jwtToken);
			String purchaseSession = JwtUtil.extractSessionId(jwtToken);

			purchase.setUsername(purchaserUsername);
			purchase.setSessionId(purchaseSession);
			
			ValidationResult validationResult = purchaseService.validatePurchase(purchase, token);
	        
	        if (validationResult.isValid()) {
	            purchaseService.placePurchase(purchase, token);
	            return ResponseEntity.ok(Map.of("message", "Purchase completed successfully."));
	        } else {
	        	Map<String, String> error = new HashMap<>();
	        	error.put("error", validationResult.getMessage());
	            return ResponseEntity.badRequest().body(Map.of("error", validationResult.getMessage()));
	        }
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
