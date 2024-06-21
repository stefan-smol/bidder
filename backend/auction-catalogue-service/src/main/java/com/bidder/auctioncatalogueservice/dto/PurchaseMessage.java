package com.bidder.auctioncatalogueservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PurchaseMessage {

    private Long auctionId;
    
    @JsonProperty("username")
    private String purchaser;
    
    private Double purchaseAmount;

    public PurchaseMessage() {
    }

    public PurchaseMessage(Long auctionId, String purchaser, Double purchaseAmount) {
        this.auctionId = auctionId;
        this.purchaser = purchaser;
        this.purchaseAmount = purchaseAmount;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public String getPurchaser() {
        return purchaser;
    }

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public static class Builder {
        private Long auctionId;
        private String purchaser;
        private Double purchaseAmount;

        public Builder() {
        }

        public Builder auctionId(Long auctionId) {
            this.auctionId = auctionId;
            return this;
        }

        public Builder purchaser(String purchaser) {
            this.purchaser = purchaser;
            return this;
        }

        public Builder purchaseAmount(Double purchaseAmount) {
            this.purchaseAmount = purchaseAmount;
            return this;
        }

        public PurchaseMessage build() {
            return new PurchaseMessage(auctionId, purchaser, purchaseAmount);
        }
    }
}
