package com.farmer_retailer.dto;

import java.util.List;

public class RetailerAnalyticsDTO {

    private long totalOrders;
    private double totalSpent;
    private long delivered;
    private long rejected;

    private List<OrderStatusDTO> orderStats;
    private List<ProductSalesDTO> productStats;

    public RetailerAnalyticsDTO() {}

    public RetailerAnalyticsDTO(long totalOrders, double totalSpent,
                                long delivered, long rejected,
                                List<OrderStatusDTO> orderStats,
                                List<ProductSalesDTO> productStats) {
        this.totalOrders = totalOrders;
        this.totalSpent = totalSpent;
        this.delivered = delivered;
        this.rejected = rejected;
        this.orderStats = orderStats;
        this.productStats = productStats;
    }

    public long getTotalOrders() { return totalOrders; }
    public double getTotalSpent() { return totalSpent; }
    public long getDelivered() { return delivered; }
    public long getRejected() { return rejected; }
    public List<OrderStatusDTO> getOrderStats() { return orderStats; }
    public List<ProductSalesDTO> getProductStats() { return productStats; }
}
