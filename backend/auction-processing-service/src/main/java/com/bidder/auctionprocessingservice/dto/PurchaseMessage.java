package com.bidder.auctionprocessingservice.dto;

public class PurchaseMessage {

    private Long auctionId;
    private String bidder;
    private Double purchaseAmount;

    public PurchaseMessage() {
    }

    public PurchaseMessage(Long auctionId, String bidder, Double purchaseAmount) {
        this.auctionId = auctionId;
        this.bidder = bidder;
        this.purchaseAmount = purchaseAmount;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public String getBidder() {
        return bidder;
    }

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public static class Builder {
        private Long auctionId;
        private String bidder;
        private Double purchaseAmount;

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

        public Builder purchaseAmount(Double purchaseAmount) {
            this.purchaseAmount = purchaseAmount;
            return this;
        }

        public PurchaseMessage build() {
            return new PurchaseMessage(auctionId, bidder, purchaseAmount);
        }
    }
}
