package com.bidder.paymentprocessingservice.model;


import java.util.Date;

import jakarta.persistence.*;


@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    
    @Column(nullable = false)
    private Long auctionId;

    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String creditCard;
    
    @Column(name = "csc", nullable = false)
    private String csc;
    
    @Column(nullable = false)
	private Date expiryDate;

    @Column(nullable = false)
    private Double totalAmount;
    
	public Payment() {
	}

	public Payment(Long orderId, Long auctionId, String username, String fullname, String creditCard, String csc,
			Date expiryDate, Double totalAmount) {
		this.orderId = orderId;
		this.auctionId = auctionId;
		this.username = username;
		this.fullname = fullname;
		this.creditCard = creditCard;
		this.csc = csc;
		this.expiryDate = expiryDate;
		this.totalAmount = totalAmount;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Long auctionId) {
		this.auctionId = auctionId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public String getCsc() {
		return csc;
	}

	public void setCsc(String csc) {
		this.csc = csc;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

}
