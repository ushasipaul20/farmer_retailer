//package com.farmer_retailer.controller;
//
//import com.farmer_retailer.dto.FarmerProfileRequest;
//import com.farmer_retailer.model.Farmer;
//import com.farmer_retailer.model.Order;
//import com.farmer_retailer.model.Product;
//import com.farmer_retailer.model.User;
//import com.farmer_retailer.repository.FarmerRepository;
//import com.farmer_retailer.repository.OrderRepository;
//import com.farmer_retailer.repository.ProductRepository;
//import com.farmer_retailer.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//@RestController
//@RequestMapping("/farmers")
//@CrossOrigin(origins = "http://localhost:5173")
//public class FarmerController {
//
//    @Autowired private FarmerRepository farmerRepository;
//    @Autowired private UserRepository userRepository;
//    @Autowired private ProductRepository productRepository;
//    @Autowired private OrderRepository orderRepository;
//
//    // =========================
//    // CREATE / UPDATE PROFILE
//    // =========================
//    @PutMapping("/{userId}/profile")
//    public Farmer saveProfile(
//            @PathVariable Long userId,
//            @RequestBody FarmerProfileRequest request
//    ) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Farmer farmer = farmerRepository.findByUser_Id(userId)
//                .orElseGet(() -> {
//                    Farmer f = new Farmer();
//                    f.setUser(user);
//                    return f;
//                });
//
//        farmer.setFarmName(request.getFarmName());
//        farmer.setPhone(request.getPhone());
//        farmer.setDistrict(request.getDistrict());
//        farmer.setProfileCompleted(true);
//
//        return farmerRepository.save(farmer);
//    }
//
//    // =========================
//    // GET PROFILE
//    // =========================
//    @GetMapping("/{userId}/profile")
//    public Farmer getProfile(@PathVariable Long userId) {
//        return farmerRepository.findByUser_Id(userId)
//                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));
//    }
//
//    // =========================
//    // ADD PRODUCT
//    // =========================
//    @PostMapping(value = "/products", consumes = "multipart/form-data")
//    public Product addProduct(
//            @RequestParam Long farmerUserId,
//            @RequestParam String name,
//            @RequestParam String description,
//            @RequestParam Double price,
//            @RequestParam Integer quantity,
//            @RequestParam String category,
//            @RequestParam String location,
//            @RequestParam MultipartFile image
//    ) throws Exception {
//
//        User farmer = userRepository.findById(farmerUserId)
//                .orElseThrow(() -> new RuntimeException("Farmer not found"));
//
//        File dir = new File("uploads");
//        if (!dir.exists()) dir.mkdirs();
//
//        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
//        Path path = Paths.get("uploads", fileName);
//        Files.write(path, image.getBytes());
//
//        Product product = new Product();
//        product.setName(name);
//        product.setDescription(description);
//        product.setPrice(price);
//        product.setQuantity(quantity);
//        product.setCategory(category);
//        product.setLocation(location);
//        product.setImageUrl("/uploads/" + fileName);
//        product.setFarmer(farmer);
//
//        return productRepository.save(product);
//    }
//
//    // =========================
//    // FARMER ORDERS
//    // =========================
//    @GetMapping("/{userId}/orders")
//    public List<Order> getOrders(@PathVariable Long userId) {
//        return orderRepository.findBySeller_Id(userId);
//    }
//}
package com.farmer_retailer.controller;

import com.farmer_retailer.dto.FarmerProfileRequest;
import com.farmer_retailer.dto.FarmerProfileResponseDTO;
import com.farmer_retailer.model.Farmer;
import com.farmer_retailer.model.Order;
import com.farmer_retailer.model.Product;
import com.farmer_retailer.model.User;
import com.farmer_retailer.repository.FarmerRepository;
import com.farmer_retailer.repository.OrderRepository;
import com.farmer_retailer.repository.ProductRepository;
import com.farmer_retailer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/farmers")
@CrossOrigin(origins = "http://localhost:5173")
public class FarmerController {

    @Autowired private FarmerRepository farmerRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;

    // =========================
    // CREATE / UPDATE PROFILE
    // =========================
    @PutMapping("/{userId}/profile")
    public Farmer saveProfile(
            @PathVariable Long userId,
            @RequestBody FarmerProfileRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Farmer farmer = farmerRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    Farmer f = new Farmer();
                    f.setUser(user);
                    return f;
                });

        farmer.setFarmName(request.getFarmName());
        farmer.setPhone(request.getPhone());
        farmer.setDistrict(request.getDistrict());
        farmer.setProfileCompleted(true);

        return farmerRepository.save(farmer);
    }

    // =========================
    // GET PROFILE
//    // =========================
//    @GetMapping("/{userId}/profile")
//    public Farmer getProfile(@PathVariable Long userId) {
//        return farmerRepository.findByUser_Id(userId)
//                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));
//    }

    @GetMapping("/{userId}/profile")
    public FarmerProfileResponseDTO getProfile(@PathVariable Long userId) {
        Farmer farmer = farmerRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));
        return new FarmerProfileResponseDTO(farmer);
    }


    // =========================
    // ADD PRODUCT
    // =========================
    @PostMapping(value = "/products", consumes = "multipart/form-data")
    public Product addProduct(
            @RequestParam Long farmerUserId,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam Integer quantity,
            @RequestParam String category,
            @RequestParam String location,
            @RequestParam MultipartFile image
    ) throws Exception {

        User farmer = userRepository.findById(farmerUserId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        File dir = new File("uploads");
        if (!dir.exists()) dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path path = Paths.get("uploads", fileName);
        Files.write(path, image.getBytes());

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setCategory(category);
        product.setLocation(location);
        product.setImageUrl("/uploads/" + fileName);
        product.setFarmer(farmer);

        return productRepository.save(product);
    }

    // =========================
    // GET ALL PRODUCTS OF A FARMER
    // =========================
//    @GetMapping("/{userId}/products")
//    public List<Product> getFarmerProducts(@PathVariable Long userId) {
//        return productRepository.findAll()
//                .stream()
//                .filter(p -> p.getFarmer() != null && p.getFarmer().getId().equals(userId))
//                .toList();
//    }
    @GetMapping("/{userId}/products")
    public List<Product> getFarmerProducts(@PathVariable Long userId) {
        return productRepository.findByFarmer_Id(userId);
    }

    // =========================
    // GET FARMER STATS
    // =========================
    @GetMapping("/{userId}/stats")
    public Map<String, Object> getFarmerStats(@PathVariable Long userId) {
        List<Product> farmerProducts = productRepository.findAll()
                .stream()
                .filter(p -> p.getFarmer() != null && p.getFarmer().getId().equals(userId))
                .toList();

        List<Order> farmerOrders = orderRepository.findBySeller_Id(userId);

        double totalRevenue = farmerOrders.stream()
                .mapToDouble(Order::getAmount)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("activeProducts", farmerProducts.size());
        stats.put("totalOrders", farmerOrders.size());
        stats.put("totalRevenue", totalRevenue);

        return stats;
    }

    // =========================
    // GET ALL ORDERS OF FARMER
    // =========================
    @GetMapping("/{userId}/orders")
    public List<Order> getOrders(@PathVariable Long userId) {
        return orderRepository.findBySeller_Id(userId);
    }
}
