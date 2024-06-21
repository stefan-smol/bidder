package com.bidder.auctioncatalogueservice;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuctionCatalogueServiceApplication {

	public static void main(String[] args) {
		
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		
		SpringApplication.run(AuctionCatalogueServiceApplication.class, args);
	}

}
