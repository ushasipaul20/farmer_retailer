package com.farmer_retailer.dto;

public class LoginResponseDTO {

    private String email;
    private String role;
    private String userId; // ✅ single ID for both farmer and retailer

    // ✅ Constructor
    public LoginResponseDTO(String email, String role, String userId) {
        this.email = email;
        this.role = role;
        this.userId = userId;
    }

    // ✅ Getters
    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getUserId() {
        return userId;
    }

    // ✅ Setters (optional)
    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
