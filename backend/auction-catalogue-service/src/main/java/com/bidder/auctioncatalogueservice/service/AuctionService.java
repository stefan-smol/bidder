package com.bidder.auctioncatalogueservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bidder.auctioncatalogueservice.model.Auction;
import com.bidder.auctioncatalogueservice.model.Auction.AuctionStatus;
import com.bidder.auctioncatalogueservice.model.Item;
import com.bidder.auctioncatalogueservice.repository.AuctionRepository;
import com.bidder.auctioncatalogueservice.repository.ItemRepository;

import jakarta.transaction.Transactional;

import com.bidder.auctioncatalogueservice.repository.AuctionSpecification;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

	@Autowired
	private AuctionRepository auctionRepository;

	@Autowired
	private ItemRepository itemRepository;

	public Auction createAuction(Auction auction) {
		return auctionRepository.save(auction);
	}

	@Transactional
	public Auction createAuctionWithItem(Auction auction, Item item) {
		System.out.println(
				"Before saving, start time: " + auction.getStartTime() + ", expiry time: " + auction.getExpiryTime());
		Item savedItem = itemRepository.save(item);
		auction.setItem(savedItem);
		return auctionRepository.save(auction);

	}

	public List<Auction> getAllAuctions() {
		return auctionRepository.findAll();
	}

	public Optional<Auction> getAuctionById(Long id) {
		return auctionRepository.findById(id);
	}

	public Auction updateAuction(Long id, Auction auctionDetails) {

		Auction auction = auctionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Auction not found for this id: " + id));

		if (!Auction.AuctionStatus.SCHEDULED.equals(auction.getStatus())) {
			throw new IllegalStateException("Auction can only be updated when its status is SCHEDULED.");
		}

		auction.setAuctionType(auctionDetails.getAuctionType());
		System.out.println("DEBUG --> Auction type updated successfully.");

		auction.setStartTime(auctionDetails.getStartTime());
		System.out.println("DEBUG --> Auction start time updated successfully.");

		auction.setExpiryTime(auctionDetails.getExpiryTime());
		System.out.println("DEBUG --> Auction expiry time updated successfully.");

		auction.setStatus(auctionDetails.getStatus());
		System.out.println("DEBUG --> Auction status updated successfully.");

		auction.setStartingPrice(auctionDetails.getStartingPrice());
		System.out.println("DEBUG --> Auction starting price updated successfully.");

		try {
			final Auction updatedAuction = auctionRepository.save(auction);
			return updatedAuction;
		} catch (Exception e) {
			System.err.println("Error saving auction: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Error saving auction", e);
		}

	}

	public Auction updateAuctionWithItem(Long id, Auction auctionDetails, Item itemDetails) {
		Auction auction = auctionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Auction not found for this id: " + id));

		auction.setAuctionType(auctionDetails.getAuctionType());
		auction.setStartTime(auctionDetails.getStartTime());
		auction.setExpiryTime(auctionDetails.getExpiryTime());
		auction.setStatus(auctionDetails.getStatus());
		auction.setStartingPrice(auctionDetails.getStartingPrice());
		auction.setPurchasePrice(auctionDetails.getPurchasePrice());

		Item item = auction.getItem();
		item.setName(itemDetails.getName());
		item.setDescription(itemDetails.getDescription());
		item.setMsrp(itemDetails.getMsrp());
		item.setCategory(itemDetails.getCategory());
		item.setBrand(itemDetails.getBrand());
		item.setItemCondition(itemDetails.getItemCondition());

		itemRepository.save(item);
		return auctionRepository.save(auction);
	}

	public Auction updateAuctionBid(Long id, Double newHighestBid, String highestBidder) {
		Auction auction = auctionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Auction not found for this id: " + id));

		auction.setCurrentHighestBid(newHighestBid);
		auction.setHighestBidder(highestBidder);
		return auctionRepository.save(auction);
	}

	public Auction updateAuctionPurchase(Long id, Double purchasePrice, String purchaser) {
		Auction auction = auctionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Auction not found for this id: " + id));

		auction.setCurrentHighestBid(purchasePrice);
		auction.setHighestBidder(purchaser);
		auction.setStatus(AuctionStatus.EXPIRED);
		return auctionRepository.save(auction);
	}

	public void deleteAuction(Long id) {
		Auction auction = auctionRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Auction not found for this id: " + id));

		auctionRepository.delete(auction);
	}

	public List<Auction> searchAuctions(String keyword) {
		return auctionRepository.findAll(AuctionSpecification.findByCriteria(keyword));
	}

	@Scheduled(fixedRate = 10000) // Check every 10 seconds
	public void updateStatusOfAuctions() {

		System.out.println("DEBUG --> CHECKING AUCTION STATUSES");

		Date currentTime = new Date();

		List<Auction> scheduledAuctions = auctionRepository.findScheduledAuctions(currentTime);
		for (Auction auction : scheduledAuctions) {
			if (auction.getStartTime().before(currentTime)) {
				auction.setStatus(AuctionStatus.RUNNING);
				auctionRepository.save(auction);
			}
		}

		List<Auction> runningAuctions = auctionRepository.findRunningAuctions(currentTime);
		for (Auction auction : runningAuctions) {
			if (auction.getExpiryTime().before(currentTime)) {
				auction.setStatus(AuctionStatus.EXPIRED);
				auctionRepository.save(auction);
			}
		}
	}
}
