package com.farmer_retailer.repository;

import com.farmer_retailer.model.FarmerVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmerVerificationRepository
        extends JpaRepository<FarmerVerification, Long> {

    Optional<FarmerVerification> findByFarmer_UserId(Long userId);
}
