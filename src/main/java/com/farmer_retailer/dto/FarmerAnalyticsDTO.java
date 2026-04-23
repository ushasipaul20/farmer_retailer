package com.farmer_retailer.dto;

import java.util.List;

public class FarmerAnalyticsDTO {

    private long totalOrders;
    private double totalRevenue;
    private long delivered;
    private long rejected;

    private List<OrderStatusDTO> orderStats;
    private List<ProductSalesDTO> productStats;

    public FarmerAnalyticsDTO() {}

    public FarmerAnalyticsDTO(long totalOrders, double totalRevenue, long delivered, long rejected,
                              List<OrderStatusDTO> orderStats, List<ProductSalesDTO> productStats) {
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.delivered = delivered;
        this.rejected = rejected;
        this.orderStats = orderStats;
        this.productStats = productStats;
    }

    // ✅ Getters and Setters
    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }

    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }

    public long getDelivered() { return delivered; }
    public void setDelivered(long delivered) { this.delivered = delivered; }

    public long getRejected() { return rejected; }
    public void setRejected(long rejected) { this.rejected = rejected; }

    public List<OrderStatusDTO> getOrderStats() { return orderStats; }
    public void setOrderStats(List<OrderStatusDTO> orderStats) { this.orderStats = orderStats; }

    public List<ProductSalesDTO> getProductStats() { return productStats; }
    public void setProductStats(List<ProductSalesDTO> productStats) { this.productStats = productStats; }
}
