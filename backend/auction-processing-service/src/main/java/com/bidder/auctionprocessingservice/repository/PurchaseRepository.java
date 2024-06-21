package com.bidder.auctionprocessingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bidder.auctionprocessingservice.model.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByAuctionId(Long auctionId);
}
