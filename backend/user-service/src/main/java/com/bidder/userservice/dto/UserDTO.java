package com.bidder.userservice.dto;

import com.bidder.userservice.model.User.Role;

import java.util.List;

import com.bidder.userservice.model.ShippingAddress;
import com.bidder.userservice.model.UserSession;

public class UserDTO {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private List<UserSession> sessions;
    private ShippingAddress shippingAddress;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<UserSession> getSessions() {
		return sessions;
	}

	public void setSessions(List<UserSession> sessions) {
		this.sessions = sessions;
	}

	public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
