package com.bidder.userservice.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class User implements UserDetails {

	public enum Role {
		USER, ADMIN;

		public List<SimpleGrantedAuthority> getAuthorities() {
			return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.name()));
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSession> sessions = new ArrayList<>();
	
	@OneToOne
    @JoinColumn(name = "shipping_id")
	@JsonManagedReference
    private ShippingAddress shippingAddress;

	public User() {
	}

	public User(String username, String email, String password, String firstName, String lastName, Role role, List<UserSession> sessions, ShippingAddress shippingAddress) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.sessions = sessions;
		this.shippingAddress = shippingAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public static UserBuilder builder() {
		return new UserBuilder();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return role.getAuthorities();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public static class UserBuilder {
		private String username;
		private String email;
		private String password;
		private String firstName;
		private String lastName;
		private Role role;
		private List<UserSession> sessions;
		private ShippingAddress shippingAddress;

		UserBuilder() {
		}

		public UserBuilder username(String username) {
			this.username = username;
			return this;
		}

		public UserBuilder email(String email) {
			this.email = email;
			return this;
		}

		public UserBuilder password(String password) {
			this.password = password;
			return this;
		}
		
		public UserBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public UserBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public UserBuilder role(Role role) {
			this.role = role;
			return this;
		}
		
		public UserBuilder sessions(List<UserSession> sessions) {
			this.sessions = sessions;
			return this;
		}
		
		public UserBuilder shippingAddress(ShippingAddress shippingAddress) {
			this.shippingAddress = shippingAddress;
			return this;
		}

		public User build() {
			return new User(username, email, password, firstName, lastName, role, sessions, shippingAddress);
		}
	}

}
