package com.farmer_retailer.dto;

public class LoginRequest {
    private String email;
    private String password;

    // Constructor (optional)
    public LoginRequest() {}

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters (required for @RequestBody mapping)
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
