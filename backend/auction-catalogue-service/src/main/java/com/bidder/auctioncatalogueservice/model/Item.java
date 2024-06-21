package com.bidder.auctioncatalogueservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Item {

	public enum ItemCondition {
		NEW, USED, REFURBISHED
	}

	public enum ItemCategory {
		APPAREL, JEWELRY, ELECTRONICS, COLLECTABLES, FURNITURE, HEALTH, COSMETICS, TOOLS, AUTOMOTIVE, OTHER
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	private Double msrp;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ItemCategory category;

	private String brand;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ItemCondition itemCondition;

	@OneToOne
	@JoinColumn(name = "auction_id")
	@JsonBackReference
	private Auction auction;

	public Item() {
	}

	public Item(String name, String description, Double msrp, ItemCategory category, String brand,
			ItemCondition itemCondition, Auction auction) {
		this.name = name;
		this.description = description;
		this.msrp = msrp;
		this.category = category;
		this.brand = brand;
		this.itemCondition = itemCondition;
		this.auction = auction;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getMsrp() {
		return msrp;
	}

	public void setMsrp(Double msrp) {
		this.msrp = msrp;
	}

	public ItemCategory getCategory() {
		return category;
	}

	public void setCategory(ItemCategory category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public ItemCondition getItemCondition() {
		return itemCondition;
	}

	public void setItemCondition(ItemCondition itemCondition) {
		this.itemCondition = itemCondition;
	}

	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	public static class ItemBuilder {
		private String name;
		private String description;
		private Double msrp;
		private ItemCategory category;
		private String brand;
		private ItemCondition itemCondition;
		private Auction auction;

		public ItemBuilder name(String name) {
			this.name = name;
			return this;
		}

		public ItemBuilder description(String description) {
			this.description = description;
			return this;
		}

		public ItemBuilder msrp(Double msrp) {
			this.msrp = msrp;
			return this;
		}

		public ItemBuilder category(ItemCategory category) {
			this.category = category;
			return this;
		}

		public ItemBuilder brand(String brand) {
			this.brand = brand;
			return this;
		}

		public ItemBuilder itemCondition(ItemCondition itemCondition) {
			this.itemCondition = itemCondition;
			return this;
		}

		public ItemBuilder auction(Auction auction) {
			this.auction = auction;
			return this;
		}

		public Item build() {
			return new Item(name, description, msrp, category, brand, itemCondition, auction);
		}
	}

}
