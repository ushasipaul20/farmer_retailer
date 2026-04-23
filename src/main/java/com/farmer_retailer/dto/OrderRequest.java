//package com.farmer_retailer.dto;
//
//public class OrderRequest {
//    private Long retailerId;
//    private Long productId;
//    private Integer quantity;
//
//    // Getters and Setters
//    public Long getRetailerId() { return retailerId; }
//    public void setRetailerId(Long retailerId) { this.retailerId = retailerId; }
//
//    public Long getProductId() { return productId; }
//    public void setProductId(Long productId) { this.productId = productId; }
//
//    public Integer getQuantity() { return quantity; }
//    public void setQuantity(Integer quantity) { this.quantity = quantity; }
//}




package com.farmer_retailer.dto;

public class OrderRequest {

    private Long retailerId;
    private Long productId;
    private Integer quantity;

    public Long getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(Long retailerId) {
        this.retailerId = retailerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
