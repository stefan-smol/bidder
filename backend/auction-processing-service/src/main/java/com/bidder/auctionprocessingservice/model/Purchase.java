package com.bidder.auctionprocessingservice.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long auctionId;
    private String sessionId;
    private String username;
    private Double purchaseAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseTime;
    
    public Purchase() {
    }

    public Purchase(Long auctionId, String sessionId, String username, Double purchaseAmount, Date purchaseTime) {
        this.auctionId = auctionId;
        this.sessionId = sessionId;
        this.username = username;
        this.purchaseAmount = purchaseAmount;
        this.purchaseTime = purchaseTime;
    }

    public Long getId() {
        return id;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public String getSessionId() {
		return sessionId;
	}

	public String getUsername() {
        return username;
    }

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public Date getPurchaseTime() {
        return purchaseTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setUsername(String username) {
        this.username = username;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public void setPurchaseTime(Date purchaseTime) {
        this.purchaseTime = purchaseTime;
    }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Purchase{" +
                "id=" + id +
                ", auctionId=" + auctionId +
                ", username='" + username + '\'' +
                ", purchaseAmount=" + purchaseAmount +
                ", purchaseTime=" + (purchaseTime != null ? sdf.format(purchaseTime) : null) +
                '}';
    }

    public static class PurchaseBuilder {
        private Long auctionId;
        private String sessionId;
        private String username;
        private Double purchaseAmount;
        private Date purchaseTime;

        public PurchaseBuilder() {
        }

        public PurchaseBuilder auctionId(Long auctionId) {
            this.auctionId = auctionId;
            return this;
        }
        
        public PurchaseBuilder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public PurchaseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public PurchaseBuilder purchaseAmount(Double purchaseAmount) {
            this.purchaseAmount = purchaseAmount;
            return this;
        }

        public PurchaseBuilder purchaseTime(Date purchaseTime) {
            this.purchaseTime = purchaseTime;
            return this;
        }

        public Purchase build() {
            return new Purchase(auctionId, sessionId, username, purchaseAmount, purchaseTime);
        }
    }
    
}
