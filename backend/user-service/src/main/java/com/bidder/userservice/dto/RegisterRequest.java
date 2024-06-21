package com.bidder.userservice.dto;

import com.bidder.userservice.model.User.Role;

public class RegisterRequest {

	private String username;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private Role role;
	private String country;
	private String city;
	private String postalCode;
	private String streetAddress;
	private String streetNumber;

	public RegisterRequest() {
	}

	public RegisterRequest(String username, String email, String password, String firstName, String lastName,
			Role role, String country, String city, String postalCode, String streetAddress, String streetNumber) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.country = country;
		this.city = city;
		this.postalCode = postalCode;
		this.streetAddress = streetAddress;
		this.streetNumber = streetNumber;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public static RegisterRequestBuilder builder() {
		return new RegisterRequestBuilder();
	}

	public static class RegisterRequestBuilder {
		private String username;
		private String email;
		private String password;
		private String firstName;
		private String lastName;
		private Role role;
		private String country;
		private String city;
		private String postalCode;
		private String streetAddress;
		private String streetNumber;

		RegisterRequestBuilder() {
		}

		public RegisterRequestBuilder username(String username) {
			this.username = username;
			return this;
		}

		public RegisterRequestBuilder email(String email) {
			this.email = email;
			return this;
		}

		public RegisterRequestBuilder password(String password) {
			this.password = password;
			return this;
		}

		public RegisterRequestBuilder firsName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public RegisterRequestBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public RegisterRequestBuilder role(Role role) {
			this.role = role;
			return this;
		}

		public RegisterRequestBuilder country(String country) {
			this.country = country;
			return this;
		}

		public RegisterRequestBuilder city(String city) {
			this.city = city;
			return this;
		}

		public RegisterRequestBuilder postalCode(String postalCode) {
			this.postalCode = postalCode;
			return this;
		}

		public RegisterRequestBuilder streetAddress(String streetAddress) {
			this.streetAddress = streetAddress;
			return this;
		}

		public RegisterRequestBuilder streetNumber(String streetNumber) {
			this.streetNumber = streetNumber;
			return this;
		}

		public RegisterRequest build() {
			return new RegisterRequest(username, email, password, firstName, lastName, role, country, city, postalCode,
					streetAddress, streetNumber);
		}
	}
}
