package com.farmer_retailer.controller;

import com.farmer_retailer.dto.ProductFeedbackDTO;
import com.farmer_retailer.model.Product;
import com.farmer_retailer.model.User;
import com.farmer_retailer.repository.FeedbackRepository;
import com.farmer_retailer.repository.ProductRepository;
import com.farmer_retailer.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductFeedbackController {

    private final FeedbackRepository feedbackRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductFeedbackController(FeedbackRepository feedbackRepository,
                                     ProductRepository productRepository,
                                     UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // Fetch feedbacks for a product (by productId)
    @GetMapping("/{productId}/feedback")
    public List<ProductFeedbackDTO> getProductFeedback(@PathVariable Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User farmer = product.getFarmer(); // ✅ Corrected getter

        return feedbackRepository.findByFarmerAndProduct(farmer, product)
                .stream()
                .map(f -> new ProductFeedbackDTO(
                        f.getRetailer().getName(),
                        f.getRating(),
                        f.getComment(),
                        f.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    // Optional: Fetch average rating for the product
    @GetMapping("/{productId}/rating")
    public double getProductAverageRating(@PathVariable Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User farmer = product.getFarmer(); // ✅ Corrected getter

        List<Integer> ratings = feedbackRepository.findByFarmerAndProduct(farmer, product)
                .stream()
                .map(f -> f.getRating())
                .toList();

        if (ratings.isEmpty()) return 0;

        return ratings.stream().mapToInt(Integer::intValue).average().orElse(0);
    }
}
