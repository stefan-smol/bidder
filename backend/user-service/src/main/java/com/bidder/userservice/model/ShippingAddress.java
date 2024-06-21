package com.bidder.userservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String postalCode;
    
    @Column(nullable = false)
    private String streetAddress;
    
    @Column(nullable = false)
    private String streetNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public ShippingAddress() {
    }

    private ShippingAddress(Long id, String country, String city, String postalCode, String streetAddress, String streetNumber, User user) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.streetAddress = streetAddress;
        this.streetNumber = streetNumber;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class Builder {
        private Long id;
        private String country;
        private String city;
        private String postalCode;
        private String streetAddress;
        private String streetNumber;
        private User user;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder streetAddress(String streetAddress) {
            this.streetAddress = streetAddress;
            return this;
        }

        public Builder streetNumber(String streetNumber) {
            this.streetNumber = streetNumber;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public ShippingAddress build() {
            return new ShippingAddress(id, country, city, postalCode, streetAddress, streetNumber, user);
        }
    }
}
