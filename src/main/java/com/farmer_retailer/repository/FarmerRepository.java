package com.farmer_retailer.repository;

import com.farmer_retailer.model.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    // 🌾 Find farmer profile by user_id
    Optional<Farmer> findByUser_Id(Long userId);
}
