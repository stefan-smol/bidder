package com.bidder.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bidder.userservice.model.ShippingAddress;


@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
	void deleteByUserId(Long userId);

}
