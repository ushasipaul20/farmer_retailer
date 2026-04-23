package com.farmer_retailer.controller;

import com.farmer_retailer.dto.OrderRequest;
import com.farmer_retailer.model.Order;
import com.farmer_retailer.model.Product;
import com.farmer_retailer.repository.ProductRepository;
import com.farmer_retailer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/retailers")
@CrossOrigin(origins = "http://localhost:5173")
public class RetailerController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    // =========================
    // BROWSE ALL PRODUCTS
    // =========================
    @GetMapping("/products")
    public List<Product> browseProducts() {
        return productRepository.findAll();
    }

    // =========================
    // PLACE ORDER
    // =========================
    @PostMapping("/orders")
    public Order placeOrder(@RequestBody OrderRequest request) {
        return orderService.placeOrder(
                request.getRetailerId(),   // buyer
                request.getProductId(),    // product
                request.getQuantity()      // quantity
        );
    }

    // =========================
    // GET RETAILER ORDERS
    // =========================
    @GetMapping("/orders")
    public List<Order> myOrders(@RequestParam Long retailerId) {
        return orderService.getOrdersByBuyer(retailerId);
    }
}
