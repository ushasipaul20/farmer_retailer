package com.farmer_retailer.repository;

import com.farmer_retailer.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 🌾 All products added by a specific farmer (user_id)
    List<Product> findByFarmer_Id(Long farmerUserId);
}
