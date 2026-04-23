package com.farmer_retailer.dto;

public class HelpTicketRequest {

    private Long userId;
    private String role;     // FARMER / RETAILER
    private String subject;
    private String message;

    // =========================
    // GETTERS & SETTERS
    // =========================

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
