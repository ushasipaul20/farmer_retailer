package com.farmer_retailer.controller;

import com.farmer_retailer.dto.ProductRequestDTO;
import com.farmer_retailer.dto.ProductResponseDTO;
import com.farmer_retailer.model.Product;
import com.farmer_retailer.model.User;
import com.farmer_retailer.repository.ProductRepository;
import com.farmer_retailer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository; // To fetch farmer by ID

    // =========================
    // GET ALL PRODUCTS
    // =========================
    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    // =========================
    // ADD NEW PRODUCT
    // =========================
//    @PostMapping("/add")
//    public ProductResponseDTO addProduct(@RequestBody ProductRequestDTO dto) {
//
//        User farmer = userRepository.findById(dto.getFarmerId())
//                .orElseThrow(() -> new RuntimeException("Farmer not found"));
//
//        Product product = new Product();
//        product.setName(dto.getName());
//        product.setDescription(dto.getDescription());
//        product.setPrice(dto.getPrice());
//        product.setQuantity(dto.getQuantity());  // ✅ updated field
//        product.setCategory(dto.getCategory());
//        product.setLocation(dto.getLocation());
//        product.setImageUrl(dto.getImageUrl());
//        product.setFarmer(farmer);
//
//        Product saved = productRepository.save(product);
//        return new ProductResponseDTO(saved);
//    }
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ProductResponseDTO addProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam Integer quantity,
            @RequestParam String category,
            @RequestParam String location,
            @RequestParam Long farmerId,
            @RequestParam MultipartFile image
    ) throws IOException {

        User farmer = userRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir + fileName);
        Files.write(imagePath, image.getBytes());

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setCategory(category);
        product.setLocation(location);
        product.setImageUrl("/uploads/" + fileName);
        product.setFarmer(farmer);

        return new ProductResponseDTO(productRepository.save(product));
    }
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ProductResponseDTO updateProduct(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam Integer quantity,
            @RequestParam String category,
            @RequestParam String location,
            @RequestParam(required = false) MultipartFile image
    ) throws IOException {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setCategory(category);
        product.setLocation(location);

        // 🔹 If a new image is uploaded, replace old one
        if (image != null && !image.isEmpty()) {
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir + fileName);
            Files.write(imagePath, image.getBytes());

            product.setImageUrl("/uploads/" + fileName);
        }

        Product updated = productRepository.save(product);
        return new ProductResponseDTO(updated);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }




    // =========================
    // ORDER PRODUCT (DECREMENT QUANTITY)
    // =========================
    @PostMapping("/order/{id}")
    public ProductResponseDTO orderProduct(@PathVariable Long id, @RequestParam Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough quantity available");
        }

        product.setQuantity(product.getQuantity() - quantity);  // ✅ decrement
        Product updatedProduct = productRepository.save(product);

        return new ProductResponseDTO(updatedProduct);
    }
}
