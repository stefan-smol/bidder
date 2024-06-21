package com.bidder.auctioncatalogueservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.bidder.auctioncatalogueservice.dto.AuctionRequest;
import com.bidder.auctioncatalogueservice.dto.Notification;
import com.bidder.auctioncatalogueservice.model.Auction;
import com.bidder.auctioncatalogueservice.model.Item;
import com.bidder.auctioncatalogueservice.service.AuctionService;
import com.bidder.auctioncatalogueservice.service.ItemService;
import com.bidder.auctioncatalogueservice.util.JwtUtil;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogue")
public class AuctionCatalogueController {

	@Autowired
	private AuctionService auctionService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private JwtUtil JwtUtil;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public String dateToIsoString(Date date) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
	    return dateFormat.format(date);
	}

	public Date convertToUTC(String localDateTimeStr, ZoneId fromZoneId) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeStr, formatter);

		ZonedDateTime zonedDateTime = localDateTime.atZone(fromZoneId);

		ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));

		return Date.from(utcDateTime.toInstant());
	}

	@GetMapping("/auctions/search")
	public List<Auction> searchAuctions(@RequestParam(value = "keyword") String keyword) {
		return auctionService.searchAuctions(keyword);
	}

	@PostMapping("/auctions")
	public ResponseEntity<?> createAuction(@RequestBody AuctionRequest auctionRequest,
			@RequestHeader(name = "Authorization", required = false) String token) {
		if (token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
		}

		try {
			String jwtToken = token.substring(7);
			String sellerUsername = JwtUtil.extractUsername(jwtToken);

			Auction auction = auctionRequest.getAuction();
			Item item = auctionRequest.getItem();

			auction.setSellerUsername(sellerUsername);

			ZoneId easternZoneId = ZoneId.of("America/New_York");
			
			String startTimeStr = dateToIsoString(auction.getStartTime());
			Date startTimeInUTC = convertToUTC(startTimeStr, easternZoneId);
			auction.setStartTime(startTimeInUTC);
			
			if (Auction.AuctionType.FORWARD.equals(auction.getAuctionType())) {
	            String expiryTimeStr = dateToIsoString(auction.getExpiryTime());
	            Date expiryTimeInUTC = convertToUTC(expiryTimeStr, easternZoneId);
	            auction.setExpiryTime(expiryTimeInUTC);
	        }

			Auction createdAuction = auctionService.createAuctionWithItem(auction, item);
			return ResponseEntity.ok(createdAuction);
		} catch (RuntimeException e) {
			System.err.println("Error parsing JWT token: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error parsing JWT token: " + e.getMessage());
		}
	}

	@GetMapping("/auctions")
	public List<Auction> getAllAuctions() {
		return auctionService.getAllAuctions();
	}

	@GetMapping("/auctions/{id}")
	public ResponseEntity<Auction> getAuctionById(@PathVariable Long id) {
		return auctionService.getAuctionById(id).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/auctions/{id}")
	public ResponseEntity<?> updateAuction(@PathVariable Long id, @RequestBody AuctionRequest auctionRequest,
			@RequestHeader(name = "Authorization", required = false) String token) {
		if (token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
		}

		try {
			String jwtToken = token.substring(7);
			String sellerUsername = JwtUtil.extractUsername(jwtToken);

			Auction auction = auctionRequest.getAuction();

			Item item = auctionRequest.getItem();
			System.out.println("DEBUG -- item data: " + item);

			auction.setSellerUsername(sellerUsername);

			Auction updatedAuction = auctionService.updateAuctionWithItem(id, auction, item);

			return ResponseEntity.ok(updatedAuction);
		} catch (RuntimeException e) {
			System.err.println("Error during auction update: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Update operation failed: " + e.getMessage());
		}
	}
	
	@PutMapping("/auctions/decrease-price/{id}")
	public ResponseEntity<?> decreasePrice(@PathVariable Long id, @RequestBody AuctionRequest auctionRequest,
			@RequestHeader(name = "Authorization", required = false) String token) {
		if (token == null || !token.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
		}

		try {
			String jwtToken = token.substring(7);
			String sellerUsername = JwtUtil.extractUsername(jwtToken);

			Auction auction = auctionRequest.getAuction();
			System.out.println("DEBUG -- auction data: " + auction);
			System.out.println("DEBUG -- auction starting price: " + auction.getStartingPrice());
			System.out.println("DEBUG -- auction highest bid: " + auction.getCurrentHighestBid());
			System.out.println("DEBUG -- auction purchase price: " + auction.getPurchasePrice());

			Item item = auctionRequest.getItem();
			System.out.println("DEBUG -- item data: " + item);

			auction.setSellerUsername(sellerUsername);

			Auction updatedAuction = auctionService.updateAuctionWithItem(id, auction, item);
			notifyBidders(updatedAuction);

			return ResponseEntity.ok(updatedAuction);
		} catch (RuntimeException e) {
			System.err.println("Error during auction update: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Update operation failed: " + e.getMessage());
		}
	}

	@DeleteMapping("/auctions/{id}")
	public ResponseEntity<Void> deleteAuction(@PathVariable Long id) {
		try {
			auctionService.deleteAuction(id);
			return ResponseEntity.ok().build();
		} catch (RuntimeException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/items")
	public Item createItem(@RequestBody Item item) {
		return itemService.createItem(item);
	}

	@GetMapping("/items")
	public List<Item> getAllItems() {
		return itemService.getAllItems();
	}

	@GetMapping("/items/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		return itemService.getItemById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/items/{id}")
	public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
		try {
			Item updatedItem = itemService.updateItem(id, itemDetails);
			return ResponseEntity.ok(updatedItem);
		} catch (RuntimeException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/items/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
		try {
			itemService.deleteItem(id);
			return ResponseEntity.ok().build();
		} catch (RuntimeException ex) {
			return ResponseEntity.notFound().build();
		}
	}
	
	private void notifyBidders(Auction auction) {
        final String notificationServiceUrl = "http://notification-serivce:8083/api/v1/notifications";
        Notification notification = new Notification(auction.getId(), auction.getPurchasePrice(), auction.getHighestBidder(), new Date(), "update-price");
        
        restTemplate.postForObject(notificationServiceUrl, notification, Notification.class);
    }
}
