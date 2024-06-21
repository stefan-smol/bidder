package com.bidder.userservice.dto;

import java.util.List;

public class UserAuthenticationInfo {
    private String username;
    private List<String> roles;

    public UserAuthenticationInfo() {
    }

    public UserAuthenticationInfo(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
