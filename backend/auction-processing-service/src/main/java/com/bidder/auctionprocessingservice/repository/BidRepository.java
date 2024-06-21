package com.bidder.auctionprocessingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bidder.auctionprocessingservice.model.Bid;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuctionId(Long auctionId);
}
