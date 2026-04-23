package com.farmer_retailer.dto;

public class ProductSalesDTO {

    private String productName;
    private long quantitySold;

    public ProductSalesDTO() {}

    public ProductSalesDTO(String productName, long quantitySold) {
        this.productName = productName;
        this.quantitySold = quantitySold;
    }

    // ✅ Getters and Setters
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public long getQuantitySold() { return quantitySold; }
    public void setQuantitySold(long quantitySold) { this.quantitySold = quantitySold; }
}
