package com.bidder.auctionprocessingservice.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuctionResponse {

    public enum AuctionStatus {
        SCHEDULED, RUNNING, EXPIRED
    }

    public enum AuctionType {
        FORWARD, DUTCH
    }

    private Long id;
    private Date startTime;
    private Date expiryTime;
    
    private AuctionStatus status;
    
    @JsonProperty("auctionType")
    private AuctionType type;
    
    private Double currentHighestBid;
    private Double purchasePrice;
    
    private Double shippingPrice;
	private Double expeditedShippingAddon;

    public AuctionResponse() {
    }

    public AuctionResponse(Long id, Date startTime, Date expiryTime, AuctionStatus status, AuctionType type, Double currentHighestBid, Double purchasePrice, Double shippingPrice, Double expeditedShippingAddon) {
        this.id = id;
        this.startTime = startTime;
        this.expiryTime = expiryTime;
        this.status = status;
        this.type = type;
        this.currentHighestBid = currentHighestBid;
        this.purchasePrice = purchasePrice;
        this.shippingPrice = shippingPrice;
        this.expeditedShippingAddon = expeditedShippingAddon;
    }

    public Long getId() {
        return id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public AuctionType getType() {
        return type;
    }

    public Double getCurrentHighestBid() {
        return currentHighestBid;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public void setType(AuctionType type) {
        this.type = type;
    }

    public void setCurrentHighestBid(Double currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
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

	public static class Builder {
        private Long id;
        private Date startTime;
        private Date expiryTime;
        private AuctionStatus status;
        private AuctionType type;
        private Double currentHighestBid;
        private Double purchasePrice;
        private Double shippingPrice;
    	private Double expeditedShippingAddon;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder startTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder expiryTime(Date expiryTime) {
            this.expiryTime = expiryTime;
            return this;
        }

        public Builder status(AuctionStatus status) {
            this.status = status;
            return this;
        }

        public Builder type(AuctionType type) {
            this.type = type;
            return this;
        }

        public Builder currentHighestBid(Double currentHighestBid) {
            this.currentHighestBid = currentHighestBid;
            return this;
        }

        public Builder purchasePrice(Double purchasePrice) {
            this.purchasePrice = purchasePrice;
            return this;
        }
        
        public Builder shippingPrice(Double shippingPrice) {
            this.shippingPrice = shippingPrice;
            return this;
        }

        public Builder expeditedShippingAddon(Double expeditedShippingAddon) {
            this.expeditedShippingAddon = expeditedShippingAddon;
            return this;
        }

        public AuctionResponse build() {
            return new AuctionResponse(id, startTime, expiryTime, status, type, currentHighestBid, purchasePrice, shippingPrice, expeditedShippingAddon);
        }
    }
}