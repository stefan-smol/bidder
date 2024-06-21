package com.bidder.auctioncatalogueservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BidMessage {

    private Long auctionId;
    
    @JsonProperty("username")
    private String bidder;
    
    private Double bidAmount;

    public BidMessage() {
    }

    public BidMessage(Long auctionId, String bidder, Double bidAmount) {
        this.auctionId = auctionId;
        this.bidder = bidder;
        this.bidAmount = bidAmount;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public String getBidder() {
        return bidder;
    }

    public Double getBidAmount() {
        return bidAmount;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }

    public void setBidAmount(Double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public static class Builder {
        private Long auctionId;
        private String bidder;
        private Double bidAmount;

        public Builder() {
        }

        public Builder auctionId(Long auctionId) {
            this.auctionId = auctionId;
            return this;
        }

        public Builder bidder(String bidder) {
            this.bidder = bidder;
            return this;
        }

        public Builder bidAmount(Double bidAmount) {
            this.bidAmount = bidAmount;
            return this;
        }

        public BidMessage build() {
            return new BidMessage(auctionId, bidder, bidAmount);
        }
    }
}


