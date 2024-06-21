package com.bidder.auctionprocessingservice.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long auctionId;
    private String sessionId;
    private String username;
    private Double bidAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date bidTime;
    
    public Bid() {}

    public Bid(Long auctionId, String sessionId, String username, Double bidAmount, Date bidTime) {
        this.auctionId = auctionId;
        this.sessionId= sessionId;
        this.username = username;
        this.bidAmount = bidAmount;
        this.bidTime = bidTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(Double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    public static class BidBuilder {
        private Long auctionId;
        private String sessionId;
        private String username;
        private Double bidAmount;
        private Date bidTime;

        public BidBuilder auctionId(Long auctionId) {
            this.auctionId = auctionId;
            return this;
        }
        public BidBuilder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public BidBuilder username(String username) {
            this.username = username;
            return this;
        }

        public BidBuilder bidAmount(Double bidAmount) {
            this.bidAmount = bidAmount;
            return this;
        }

        public BidBuilder bidTime(Date bidTime) {
            this.bidTime = bidTime;
            return this;
        }

        public Bid build() {
            return new Bid(auctionId, sessionId, username, bidAmount, bidTime);
        }
    }

}

