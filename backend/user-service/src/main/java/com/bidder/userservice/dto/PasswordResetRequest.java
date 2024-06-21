package com.bidder.userservice.dto;

public class PasswordResetRequest {
	
	 private String username;
	    private String password;

	    public PasswordResetRequest() {
	    }

	    public PasswordResetRequest(String username, String password) {
	        this.username = username;
	        this.password = password;
	    }


	    public String getUsername() {
	        return username;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public static PasswordResetRequestBuilder builder() {
	        return new PasswordResetRequestBuilder();
	    }

	    public static class PasswordResetRequestBuilder {
	        private String username;
	        private String password;

	        PasswordResetRequestBuilder() {
	        }

			public PasswordResetRequestBuilder username(String username) {
	            this.username = username;
	            return this;
	        }

	        public PasswordResetRequestBuilder password(String password) {
	            this.password = password;
	            return this;
	        }

	        public PasswordResetRequest build() {
	            return new PasswordResetRequest(username, password);
	        }
	    }

}
