package com.farmer_retailer.dto;

public class ProductRequestDTO {

    private String name;
    private String description;
    private Double price;
    private Integer quantity; // ✅ matches ER diagram
    private String category;
    private String location;
    private String imageUrl;
    private Long farmerId;

    // ===== GETTERS =====
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public Integer getQuantity() { return quantity; } // ✅ fixed
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public String getImageUrl() { return imageUrl; }
    public Long getFarmerId() { return farmerId; }

    // ===== SETTERS =====
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; } // ✅ fixed
    public void setCategory(String category) { this.category = category; }
    public void setLocation(String location) { this.location = location; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setFarmerId(Long farmerId) { this.farmerId = farmerId; }
}
