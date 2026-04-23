//package com.farmer_retailer.service;
//
//import com.farmer_retailer.model.Product;
//import com.farmer_retailer.model.User;
//import com.farmer_retailer.repository.ProductRepository;
//import com.farmer_retailer.repository.UserRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ProductService {
//
//    private final ProductRepository productRepository;
//    private final UserRepository userRepository;
//    private final FarmerVerificationService farmerVerificationService;
//
//    public ProductService(
//            ProductRepository productRepository,
//            UserRepository userRepository,
//            FarmerVerificationService farmerVerificationService
//    ) {
//        this.productRepository = productRepository;
//        this.userRepository = userRepository;
//        this.farmerVerificationService = farmerVerificationService;
//    }
//
//    // ==============================
//    // ADD PRODUCT (VERIFICATION CHECK)
//    // ==============================
//    public Product addProduct(Product product, Long farmerUserId) {
//
//        // 🔒 MOST IMPORTANT CHECK
//        if (!farmerVerificationService.isFarmerVerified(farmerUserId)) {
//            throw new RuntimeException(
//                    "Farmer is not verified. Cannot add products."
//            );
//        }
//
//        User farmer = userRepository.findById(farmerUserId)
//                .orElseThrow(() -> new RuntimeException("Farmer not found"));
//
//        product.setFarmer(farmer); // seller
//        return productRepository.save(product);
//    }
//}

package com.farmer_retailer.service;

import com.farmer_retailer.model.Product;
import com.farmer_retailer.model.User;
import com.farmer_retailer.repository.ProductRepository;
import com.farmer_retailer.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FarmerVerificationService farmerVerificationService;

    public ProductService(
            ProductRepository productRepository,
            UserRepository userRepository,
            FarmerVerificationService farmerVerificationService
    ) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.farmerVerificationService = farmerVerificationService;
    }

    // 🔒 MOST IMPORTANT RULE
    public Product addProduct(Product product, Long farmerUserId) {

        if (!farmerVerificationService.isFarmerVerified(farmerUserId)) {
            throw new RuntimeException(
                    "Farmer is not verified. Cannot add products."
            );
        }

        User farmer = userRepository.findById(farmerUserId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        product.setFarmer(farmer);
        return productRepository.save(product);
    }
}
