package com.bidder.userservice.dto;

public class SessionUpdateRequest {
    private Long auctionId;
    
	public SessionUpdateRequest() {
	}

	public SessionUpdateRequest(Long auctionId) {
		this.auctionId = auctionId;
	}

	public Long getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Long auctionId) {
		this.auctionId = auctionId;
	}

}
