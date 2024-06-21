package com.bidder.auctioncatalogueservice.dto;

import java.util.Date;

public class Notification {
	 private String id;
	    private Long auctionId;
	    private Double bidAmount;
	    private String bidderUsername;
	    private Date timestamp;
	    private String notifcationType;
	    
	    public Notification() {
	    }
	    
	    public Notification(Long auctionId, Double bidAmount, String bidderUsername, Date timestamp, String notifcationType) {
	        this.auctionId = auctionId;
	        this.bidAmount= bidAmount;
	        this.bidderUsername = bidderUsername;
	        this.timestamp = timestamp;
	        this.notifcationType = notifcationType;
	    }
	    
	    public String getId() {
	        return id;
	    }
	    
	    public void setId(String id) {
	        this.id = id;
	    }
	    
	    public Long getAuctionId() {
	        return auctionId;
	    }
	    
	    public void setAuctionId(Long auctionId) {
	        this.auctionId = auctionId;
	    }
	    
	    
	    public Double getBidAmount() {
			return bidAmount;
		}

		public void setBidAmount(Double bidAmount) {
			this.bidAmount = bidAmount;
		}

		public String getBidderUsername() {
			return bidderUsername;
		}

		public void setBidderUsername(String bidderUsername) {
			this.bidderUsername = bidderUsername;
		}

		public Date getTimestamp() {
	        return timestamp;
	    }
	    
	    public void setTimestamp(Date timestamp) {
	        this.timestamp = timestamp;
	    }

		public String getNotifcationType() {
			return notifcationType;
		}

		public void setNotifcationType(String notifcationType) {
			this.notifcationType = notifcationType;
		}
}

