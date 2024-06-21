package com.bidder.auctionprocessingservice.dto;

public class SessionResponse {
    private String sessionId;
    private String username;
    private Long auctionId;
    
	public SessionResponse() {
	}
    
	public SessionResponse(String sessionId, String username, Long auctionId) {
		this.sessionId = sessionId;
		this.username = username;
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
	public Long getAuctionId() {
		return auctionId;
	}
	public void setAuctionId(Long auctionId) {
		this.auctionId = auctionId;
	}

}
