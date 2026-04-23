//package com.farmer_retailer.controller;
//
//import com.farmer_retailer.model.Order;
//import com.farmer_retailer.model.Product;
//import com.farmer_retailer.model.Role;
//import com.farmer_retailer.model.User;
//import com.farmer_retailer.repository.OrderRepository;
//import com.farmer_retailer.repository.ProductRepository;
//import com.farmer_retailer.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/admin")
//@CrossOrigin(origins = "http://localhost:5173")
//public class AdminController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    // =========================
//    // ✅ GET ALL FARMERS
//    // =========================
//    @GetMapping("/farmers")
//    public List<User> getAllFarmers() {
//        return userRepository.findByRole(Role.FARMER);
//    }
//
//    // =========================
//    // ✅ GET ALL RETAILERS
//    // =========================
//    @GetMapping("/retailers")
//    public List<User> getAllRetailers() {
//        return userRepository.findByRole(Role.RETAILER);
//    }
//
//    // =========================
//    // ✅ GET ALL PRODUCTS
//    // =========================
//    @GetMapping("/products")
//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }
//
//    // =========================
//    // ✅ GET ALL ORDERS
//    // =========================
//    @GetMapping("/orders")
//    public List<Order> getAllOrders() {
//        return orderRepository.findAll();
//    }
//
//    // =========================
//    // ✅ DELETE ENDPOINTS
//    // =========================
//    @DeleteMapping("/farmers/{id}")
//    public void deleteFarmer(@PathVariable Long id) {
//        userRepository.deleteById(id);
//    }
//
//    @DeleteMapping("/retailers/{id}")
//    public void deleteRetailer(@PathVariable Long id) {
//        userRepository.deleteById(id);
//    }
//
//    @DeleteMapping("/products/{id}")
//    public void deleteProduct(@PathVariable Long id) {
//        productRepository.deleteById(id);
//    }
//
//    @DeleteMapping("/orders/{id}")
//    public void deleteOrder(@PathVariable Long id) {
//        orderRepository.deleteById(id);
//    }
//
//}




package com.farmer_retailer.controller;

import com.farmer_retailer.model.*;
import com.farmer_retailer.repository.OrderRepository;
import com.farmer_retailer.repository.ProductRepository;
import com.farmer_retailer.repository.UserRepository;
import com.farmer_retailer.service.FarmerVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final FarmerVerificationService verificationService;

    @Autowired
    public AdminController(
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            FarmerVerificationService verificationService
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.verificationService = verificationService;
    }

    // =========================
    // ✅ GET ALL FARMERS
    // =========================
    @GetMapping("/farmers")
    public List<User> getAllFarmers() {
        return userRepository.findByRole(Role.FARMER);
    }

    // =========================
    // ✅ GET ALL RETAILERS
    // =========================
    @GetMapping("/retailers")
    public List<User> getAllRetailers() {
        return userRepository.findByRole(Role.RETAILER);
    }

    // =========================
    // ✅ GET ALL PRODUCTS
    // =========================
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // =========================
    // ✅ GET ALL ORDERS
    // =========================
    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // =========================
    // ✅ DELETE ENDPOINTS
    // =========================
    @DeleteMapping("/farmers/{id}")
    public void deleteFarmer(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @DeleteMapping("/retailers/{id}")
    public void deleteRetailer(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    @DeleteMapping("/orders/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
    }

    // =========================
    // ✅ FARMER VERIFICATION
    // =========================
    @PostMapping("/verify/{farmerId}")
    public FarmerVerification verifyFarmer(@PathVariable Long farmerId) {
        return verificationService.approve(farmerId);
    }

    @PostMapping("/reject/{farmerId}")
    public FarmerVerification rejectFarmer(@PathVariable Long farmerId) {
        return verificationService.reject(farmerId);
    }
}
