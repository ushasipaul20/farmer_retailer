//package com.farmer_retailer.model;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "orders")
//public class Order {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "buyer_user_id", nullable = false)
//    private User buyer;
//
//    @ManyToOne
//    @JoinColumn(name = "seller_user_id", nullable = false)
//    private User seller;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;
//
//    private Integer quantity;
//    private Double amount;
//
//    @Column(name = "order_date", nullable = false)
//    private LocalDateTime orderDate;
//
//    private String status; // PENDING, ACCEPTED, DELIVERED
//
//    public Order() {}
//
//    // ===== Getters & Setters =====
//    public Long getId() { return id; }
//
//    public User getBuyer() { return buyer; }
//    public void setBuyer(User buyer) { this.buyer = buyer; }
//
//    public User getSeller() { return seller; }
//    public void setSeller(User seller) { this.seller = seller; }
//
//    public Product getProduct() { return product; }
//    public void setProduct(Product product) { this.product = product; }
//
//    public Integer getQuantity() { return quantity; }
//    public void setQuantity(Integer quantity) { this.quantity = quantity; }
//
//    public Double getAmount() { return amount; }
//    public void setAmount(Double amount) { this.amount = amount; }
//
//    public LocalDateTime getOrderDate() { return orderDate; }
//    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
//
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//}




package com.farmer_retailer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🛒 Buyer → User with role RETAILER
    @ManyToOne
    @JoinColumn(name = "buyer_user_id", nullable = false)
    @JsonIgnoreProperties({"password", "farmer", "retailer"})
    private User buyer;

    // 🌾 Seller → User with role FARMER
    @ManyToOne
    @JoinColumn(name = "seller_user_id", nullable = false)
    @JsonIgnoreProperties({"password", "farmer", "retailer"})
    private User seller;

    // 📦 Product being ordered
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"user"})
    private Product product;

    private Integer quantity;

    private Double amount;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    // PENDING, ACCEPTED, DELIVERED, CANCELLED
    private String status = "PENDING_PAYMENT";

    // ===== Constructors =====
    public Order() {}

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
