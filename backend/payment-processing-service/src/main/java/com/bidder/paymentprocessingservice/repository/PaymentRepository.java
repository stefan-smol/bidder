package com.bidder.paymentprocessingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bidder.paymentprocessingservice.model.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	List<Payment> findByUsername(String username);

	Payment findByAuctionId(Long auctionId);
}
