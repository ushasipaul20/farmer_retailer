package com.farmer_retailer.dto;

import java.time.LocalDateTime;

public class ProductFeedbackDTO {
    private String retailerName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public ProductFeedbackDTO() {}

    public ProductFeedbackDTO(String retailerName, int rating, String comment, LocalDateTime createdAt) {
        this.retailerName = retailerName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getRetailerName() { return retailerName; }
    public void setRetailerName(String retailerName) { this.retailerName = retailerName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
