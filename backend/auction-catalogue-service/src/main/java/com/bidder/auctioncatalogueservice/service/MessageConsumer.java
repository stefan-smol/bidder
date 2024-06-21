package com.bidder.auctioncatalogueservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.bidder.auctioncatalogueservice.config.RabbitMQConfig;
import com.bidder.auctioncatalogueservice.dto.BidMessage;
import com.bidder.auctioncatalogueservice.dto.Notification;
import com.bidder.auctioncatalogueservice.dto.PurchaseMessage;
import com.bidder.auctioncatalogueservice.model.Auction;
import com.bidder.auctioncatalogueservice.repository.AuctionRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class MessageConsumer {

	@Autowired
	private AuctionRepository auctionRepository;

	@Autowired
	private AuctionService auctionService;

	@Autowired
	private RestTemplate restTemplate;

    private void notifyBidders(Auction auction) {
        final String notificationServiceUrl = "http://notification-serivce:8083/api/v1/notifications";
        Notification notification = new Notification(auction.getId(), auction.getCurrentHighestBid(), auction.getHighestBidder(), new Date(), "bid");
        
        restTemplate.postForObject(notificationServiceUrl, notification, Notification.class);
    }

    private void notifyPotentialPurchasers(Auction auction) {
        final String notificationServiceUrl = "http://notification-serivce:8083/api/v1/notifications";
        Notification notification = new Notification(auction.getId(), auction.getCurrentHighestBid(), auction.getHighestBidder(), new Date(), "purchase");
        
        restTemplate.postForObject(notificationServiceUrl, notification, Notification.class);
    }

	@RabbitListener(queues = RabbitMQConfig.FORWARD_QUEUE)
	public void handleNewHighestBid(BidMessage bidMessage) {
		if (bidMessage == null) {
			System.out.println("Received null BidMessage");
			return;
		}

		if (bidMessage.getAuctionId() == null) {
			System.out.println("BidMessage auctionId is null");
			return;
		}
		if (bidMessage.getBidAmount() == null) {
			System.out.println("BidMessage bidAmount is null");
			return;
		}
		if (bidMessage.getBidder() == null) {
			System.out.println("BidMessage bidder is null");
			return;
		}

		Optional<Auction> auctionOptional = auctionRepository.findById(bidMessage.getAuctionId());
		if (!auctionOptional.isPresent()) {
			System.out.println("Auction with ID " + bidMessage.getAuctionId() + " not found");
			return;
		}

		auctionOptional.ifPresent(auction -> {
			if (auction.getCurrentHighestBid() == null || bidMessage.getBidAmount() > auction.getCurrentHighestBid()) {
				Auction upToDateAuction = auctionService.updateAuctionBid(auction.getId(), bidMessage.getBidAmount(), bidMessage.getBidder());
				
				notifyBidders(upToDateAuction);
			}
		});
	}

	@RabbitListener(queues = RabbitMQConfig.DUTCH_QUEUE)
	public void handlePurchaseRequest(PurchaseMessage purchaseMessage) {
		if (purchaseMessage == null) {
			System.out.println("Received null BidMessage for Dutch auction");
			return;
		}

		Optional<Auction> auctionOptional = auctionRepository.findById(purchaseMessage.getAuctionId());
		if (!auctionOptional.isPresent()) {
			System.out.println("Dutch Auction with ID " + purchaseMessage.getAuctionId() + " not found");
			return;
		}

		auctionOptional.ifPresent(auction -> {
			if (purchaseMessage.getPurchaseAmount() != null && purchaseMessage.getPurchaseAmount() >= auction.getPurchasePrice()) {
				auction.setHighestBidder(purchaseMessage.getPurchaser());
				auction.setStatus(Auction.AuctionStatus.EXPIRED);
				auctionService.updateAuctionPurchase(auction.getId(), purchaseMessage.getPurchaseAmount(), purchaseMessage.getPurchaser());
				notifyPotentialPurchasers(auction);
			} else {
				System.out.println("Purchase amount " + purchaseMessage.getPurchaseAmount()
						+ " is not enough to purchase the item in Dutch Auction " + auction.getId());
			}
		});
	}
}
