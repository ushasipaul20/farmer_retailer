package com.farmer_retailer.controller;

import com.farmer_retailer.dto.OrderStatusDTO;
import com.farmer_retailer.dto.ProductSalesDTO;
import com.farmer_retailer.dto.RetailerAnalyticsDTO;
import com.farmer_retailer.model.Order;
import com.farmer_retailer.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/retailers")
@CrossOrigin(origins = "http://localhost:5173")
public class RetailerAnalyticsController {

    private final OrderRepository orderRepository;

    public RetailerAnalyticsController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/{retailerId}/analytics")
    public RetailerAnalyticsDTO getRetailerAnalytics(@PathVariable Long retailerId) {

        // =========================
        // ✅ BASIC COUNTS
        // =========================
        long totalOrders = orderRepository.countByBuyer_Id(retailerId);
        long delivered = orderRepository.countByBuyer_IdAndStatus(retailerId, "DELIVERED");
        long rejected = orderRepository.countByBuyer_IdAndStatus(retailerId, "REJECTED");

        // ✅ Total amount spent (DELIVERED only)
        double totalSpent = orderRepository.sumRevenueByBuyerUserId(retailerId);

        // =========================
        // ✅ ORDER STATUS STATS
        // =========================
        List<OrderStatusDTO> orderStats = new ArrayList<>();
        String[] statuses = {"PENDING", "ACCEPTED", "PROCESSED", "DELIVERED", "REJECTED"};

        for (String status : statuses) {
            long count = orderRepository.countByBuyer_IdAndStatus(retailerId, status);
            orderStats.add(new OrderStatusDTO(status, count));
        }

        // =========================
        // ✅ PRODUCT PURCHASE STATS
        // =========================
        List<Order> deliveredOrders =
                orderRepository.findByBuyer_IdAndStatus(retailerId, "DELIVERED");

        List<ProductSalesDTO> productStats = new ArrayList<>();

        deliveredOrders.stream()
                .map(Order::getProduct)
                .distinct()
                .forEach(product -> {
                    long totalPurchased = deliveredOrders.stream()
                            .filter(o -> o.getProduct().getId().equals(product.getId()))
                            .mapToLong(Order::getQuantity)
                            .sum();

                    productStats.add(
                            new ProductSalesDTO(product.getName(), totalPurchased)
                    );
                });

        return new RetailerAnalyticsDTO(
                totalOrders,
                totalSpent,
                delivered,
                rejected,
                orderStats,
                productStats
        );
    }
}
