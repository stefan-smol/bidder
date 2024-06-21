package com.bidder.auctioncatalogueservice.dto;

import com.bidder.auctioncatalogueservice.model.Auction;
import com.bidder.auctioncatalogueservice.model.Item;

public class AuctionRequest {
    private Auction auction;
    private Item item;

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

