package com.bidder.auctioncatalogueservice.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Entity
public class Auction {

	public enum AuctionStatus {
		SCHEDULED, RUNNING, EXPIRED
	}

	public enum AuctionType {
		FORWARD, DUTCH
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	private Date createdAt;

	@UpdateTimestamp
	private Date updatedAt;

	@Enumerated(EnumType.STRING)
	private AuctionType auctionType;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryTime;

	@Enumerated(EnumType.STRING)
	private AuctionStatus status;

	@Column(columnDefinition = "DOUBLE DEFAULT 1.0")
	private Double startingPrice = 1.0;

	private Double currentHighestBid;
	
	private String highestBidder;

	private Double purchasePrice;

	private Double shippingPrice;
	
	private Double expeditedShippingAddon;
	
	@Column(nullable = false)
	private String sellerUsername;

	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "item_id")
	@JsonManagedReference
	private Item item;

	public Auction() {
	}

	public Auction(AuctionType auctionType, Date startTime, Date expiryTime, AuctionStatus status, Double startingPrice,
			Double currentHighestBid, String highestBidder, Double purchasePrice, Double shippingPrice, Double expeditedShippingAddon, String sellerUsername,
			Item item) {
		this.auctionType = auctionType;
		this.startTime = startTime;
		this.expiryTime = expiryTime;
		this.status = status;
		this.startingPrice = startingPrice;
		this.currentHighestBid = currentHighestBid;
		this.highestBidder = highestBidder;
		this.purchasePrice = purchasePrice;
		this.shippingPrice = shippingPrice;
		this.expeditedShippingAddon = expeditedShippingAddon;
		this.sellerUsername = sellerUsername;
		this.item = item;
	}

	public Long getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public AuctionType getAuctionType() {
		return auctionType;
	}

	public void setAuctionType(AuctionType auctionType) {
		this.auctionType = auctionType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}

	public AuctionStatus getStatus() {
		return status;
	}

	public void setStatus(AuctionStatus status) {
		this.status = status;
	}

	public Double getStartingPrice() {
		return startingPrice;
	}

	public void setStartingPrice(Double startingPrice) {
		this.startingPrice = startingPrice;
	}

	public Double getCurrentHighestBid() {
		return currentHighestBid;
	}

	public void setCurrentHighestBid(Double currentHighestBid) {
		this.currentHighestBid = currentHighestBid;
	}
	
	public String getHighestBidder() {
        return highestBidder;
    }

    public void setHighestBidder(String highestBidder) {
        this.highestBidder = highestBidder;
    }

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}


	public Double getShippingPrice() {
		return shippingPrice;
	}

	public void setShippingPrice(Double shippingPrice) {
		this.shippingPrice = shippingPrice;
	}

	public Double getExpeditedShippingAddon() {
		return expeditedShippingAddon;
	}

	public void setExpeditedShippingAddon(Double expeditedShippingAddon) {
		this.expeditedShippingAddon = expeditedShippingAddon;
	}

	public String getSellerUsername() {
		return sellerUsername;
	}

	public void setSellerUsername(String sellerUsername) {
		this.sellerUsername = sellerUsername;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	 public static class AuctionBuilder {
	        private AuctionType auctionType;
	        private Date startTime;
	        private Date expiryTime;
	        private AuctionStatus status;
	        private Double startingPrice;
	        private Double currentHighestBid;
	        private String highestBidder;
	        private Double purchasePrice;
	        private Double shippingPrice;
	    	private Double expeditedShippingAddon;
	        private String sellerUsername;
	        private Item item;

	        public AuctionBuilder auctionType(AuctionType auctionType) {
	            this.auctionType = auctionType;
	            return this;
	        }

	        public AuctionBuilder startTime(Date startTime) {
	            this.startTime = startTime;
	            return this;
	        }

	        public AuctionBuilder expiryTime(Date expiryTime) {
	            this.expiryTime = expiryTime;
	            return this;
	        }

	        public AuctionBuilder status(AuctionStatus status) {
	            this.status = status;
	            return this;
	        }

	        public AuctionBuilder startingPrice(Double startingPrice) {
	            this.startingPrice = startingPrice;
	            return this;
	        }

	        public AuctionBuilder currentHighestBid(Double currentHighestBid) {
	            this.currentHighestBid = currentHighestBid;
	            return this;
	        }
	        
	        public AuctionBuilder highestBidder(String highestBidder) {
	            this.highestBidder = highestBidder;
	            return this;
	        }

	        public AuctionBuilder purchasePrice(Double purchasePrice) {
	            this.purchasePrice = purchasePrice;
	            return this;
	        }

	        public AuctionBuilder shippingPrice(Double shippingPrice) {
	            this.shippingPrice = shippingPrice;
	            return this;
	        }
	        
	        public AuctionBuilder expeditedShippingAddon(Double expeditedShippingAddon) {
	            this.expeditedShippingAddon = expeditedShippingAddon;
	            return this;
	        }

	        public AuctionBuilder sellerUsername(String sellerUsername) {
	            this.sellerUsername = sellerUsername;
	            return this;
	        }

	        public AuctionBuilder item (Item item) {
	            this.item = item;
	            return this;
	        }

	        public Auction build() {
	            return new Auction(auctionType, startTime, expiryTime, status, startingPrice, currentHighestBid,
	                               highestBidder, purchasePrice, shippingPrice, expeditedShippingAddon, sellerUsername, item);
	        }

	    }
}
