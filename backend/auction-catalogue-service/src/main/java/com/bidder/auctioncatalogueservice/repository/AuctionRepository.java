package com.bidder.auctioncatalogueservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bidder.auctioncatalogueservice.model.Auction;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long>, JpaSpecificationExecutor<Auction> {
	
	@Query("SELECT a FROM Auction a WHERE a.startTime <= :currentTime AND a.status = 'SCHEDULED'")
    List<Auction> findScheduledAuctions(Date currentTime);

    @Query("SELECT a FROM Auction a WHERE a.expiryTime <= :currentTime AND a.status = 'RUNNING'")
    List<Auction> findRunningAuctions(Date currentTime);
	
}
