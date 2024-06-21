package com.bidder.userservice.model;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", unique = true, nullable = false)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    
    @Column(name = "auction_id")
    private Long auctionId;

    public UserSession() {
    }

    public UserSession(Long id, String sessionId, User user, Date createdAt, Long auctionId) {
        this.id = id;
        this.sessionId = sessionId;
        this.user = user;
        this.createdAt = createdAt;
        this.auctionId = auctionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Long auctionId) {
		this.auctionId = auctionId;
	}

	public static class Builder {
        private Long id;
        private String sessionId;
        private User user;
        private Date createdAt;
        private Long auctionId;

        public Builder() {
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setSessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Builder setAuctionId(Long auctionId) {
            this.auctionId = auctionId;
            return this;
        }

        public UserSession build() {
            return new UserSession(id, sessionId, user, createdAt, auctionId);
        }
    }
}

