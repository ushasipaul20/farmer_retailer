//package com.farmer_retailer.repository;
//
//import com.farmer_retailer.dto.ProductSalesDTO;
//import com.farmer_retailer.model.Order;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//
//public interface OrderRepository extends JpaRepository<Order, Long> {
//
//    // 🛒 Orders placed by a retailer (buyer)
//    List<Order> findByBuyer_Id(Long buyerUserId);
//
//    // 🌾 Orders received by a farmer (seller)
//    List<Order> findBySeller_Id(Long sellerUserId);
//
//    // 🌾 Orders by farmer and status
//    List<Order> findBySeller_IdAndStatus(Long sellerUserId, String status);
//
//    // 📊 Analytics: count orders received by a farmer
//    long countBySeller_Id(Long sellerUserId);
//
//    long countBySeller_IdAndStatus(Long sellerUserId, String status);
//
//    // 📊 Analytics: total revenue for a farmer (DELIVERED orders)
//    @Query("""
//        SELECT COALESCE(SUM(o.amount), 0)
//        FROM Order o
//        WHERE o.seller.id = :sellerUserId
//        AND o.status = 'DELIVERED'
//    """)
//    double sumRevenueBySellerUserId(@Param("sellerUserId") Long sellerUserId);
//
//    // 📊 Analytics: product-wise quantity sold by farmer (DELIVERED orders only)
//    @Query("""
//    SELECT new com.farmer_retailer.dto.ProductSalesDTO(
//        o.product.name,
//        SUM(o.quantity)
//    )
//    FROM Order o
//    WHERE o.seller.id = :sellerUserId
//    AND o.status = 'DELIVERED'
//    GROUP BY o.product.name
//""")
//    List<ProductSalesDTO> findProductSalesBySeller(
//            @Param("sellerUserId") Long sellerUserId
//    );
//
//}



package com.farmer_retailer.repository;

import com.farmer_retailer.dto.ProductSalesDTO;
import com.farmer_retailer.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // =========================
    // 🛒 RETAILER (BUYER) ORDERS
    // =========================

    // All orders placed by a retailer
    List<Order> findByBuyer_Id(Long buyerId);

    // Orders placed by retailer filtered by status
    List<Order> findByBuyer_IdAndStatus(Long buyerId, String status);

    // Count orders placed by retailer
    long countByBuyer_Id(Long buyerId);

    // Count orders by retailer and status
    long countByBuyer_IdAndStatus(Long buyerId, String status);

    // Total amount spent by retailer (DELIVERED orders only)
    @Query("""
        SELECT COALESCE(SUM(o.amount), 0)
        FROM Order o
        WHERE o.buyer.id = :buyerId
        AND o.status = 'DELIVERED'
    """)
    double sumRevenueByBuyerUserId(@Param("buyerId") Long buyerId);

    // Product-wise quantity purchased by retailer (DELIVERED orders only)
    @Query("""
        SELECT new com.farmer_retailer.dto.ProductSalesDTO(
            o.product.name,
            SUM(o.quantity)
        )
        FROM Order o
        WHERE o.buyer.id = :buyerId
        AND o.status = 'DELIVERED'
        GROUP BY o.product.name
    """)
    List<ProductSalesDTO> findProductSalesByBuyer(@Param("buyerId") Long buyerId);


    // =========================
    // 🌾 FARMER (SELLER) ORDERS
    // =========================

    // All orders received by a farmer
    List<Order> findBySeller_Id(Long sellerId);

    // Orders received by farmer filtered by status
    List<Order> findBySeller_IdAndStatus(Long sellerId, String status);

    // Count orders received by farmer
    long countBySeller_Id(Long sellerId);

    // Count orders received by farmer filtered by status
    long countBySeller_IdAndStatus(Long sellerId, String status);

    // Total revenue for a farmer (DELIVERED orders only)
    @Query("""
        SELECT COALESCE(SUM(o.amount), 0)
        FROM Order o
        WHERE o.seller.id = :sellerId
        AND o.status = 'DELIVERED'
    """)
    double sumRevenueBySellerUserId(@Param("sellerId") Long sellerId);

    // Product-wise quantity sold by farmer (DELIVERED orders only)
    @Query("""
        SELECT new com.farmer_retailer.dto.ProductSalesDTO(
            o.product.name,
            SUM(o.quantity)
        )
        FROM Order o
        WHERE o.seller.id = :sellerId
        AND o.status = 'DELIVERED'
        GROUP BY o.product.name
    """)
    List<ProductSalesDTO> findProductSalesBySeller(@Param("sellerId") Long sellerId);
}
