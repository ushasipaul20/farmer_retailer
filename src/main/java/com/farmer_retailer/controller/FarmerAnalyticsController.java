package com.farmer_retailer.controller;

import com.farmer_retailer.dto.FarmerAnalyticsDTO;
import com.farmer_retailer.dto.OrderStatusDTO;
import com.farmer_retailer.dto.ProductSalesDTO;
import com.farmer_retailer.model.Order;
import com.farmer_retailer.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/farmers")
@CrossOrigin(origins = "http://localhost:5173")
public class FarmerAnalyticsController {

    private final OrderRepository orderRepository;

    public FarmerAnalyticsController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/{farmerId}/analytics")
    public FarmerAnalyticsDTO getFarmerAnalytics(@PathVariable Long farmerId) {

        // =========================
        // ✅ BASIC COUNTS
        // =========================
        long totalOrders = orderRepository.countBySeller_Id(farmerId);
        long delivered = orderRepository.countBySeller_IdAndStatus(farmerId, "DELIVERED");
        long rejected = orderRepository.countBySeller_IdAndStatus(farmerId, "REJECTED");

        // ✅ Revenue ONLY from DELIVERED orders
        double totalRevenue = orderRepository.sumRevenueBySellerUserId(farmerId);

        // =========================
        // ✅ ORDER STATUS STATS
        // =========================
        List<OrderStatusDTO> orderStats = new ArrayList<>();
        String[] statuses = {"PENDING_PAYMENT", "ACCEPTED", "PROCESSED", "DELIVERED", "REJECTED"};

        for (String status : statuses) {
            long count = orderRepository.countBySeller_IdAndStatus(farmerId, status);
            orderStats.add(new OrderStatusDTO(status, count));
        }

        // =========================
        // ✅ PRODUCT SALES (DELIVERED ONLY)
        // =========================
        List<Order> deliveredOrders = orderRepository.findBySeller_IdAndStatus(farmerId, "DELIVERED");

        List<ProductSalesDTO> productStats = new ArrayList<>();

        deliveredOrders.stream()
                .map(Order::getProduct)
                .distinct()
                .forEach(product -> {
                    long totalSold = deliveredOrders.stream()
                            .filter(o -> o.getProduct().getId().equals(product.getId()))
                            .mapToLong(Order::getQuantity)
                            .sum();

                    productStats.add(new ProductSalesDTO(product.getName(), totalSold));
                });

        return new FarmerAnalyticsDTO(
                totalOrders,
                totalRevenue,
                delivered,
                rejected,
                orderStats,
                productStats
        );
    }
}
