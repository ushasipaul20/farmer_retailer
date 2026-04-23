package com.farmer_retailer.repository;

import com.farmer_retailer.model.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RetailerRepository extends JpaRepository<Retailer, Long> {

    // 🛒 Find retailer profile by user_id
    Optional<Retailer> findByUser_Id(Long userId);
}
