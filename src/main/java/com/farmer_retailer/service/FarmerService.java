package com.farmer_retailer.service;

import com.farmer_retailer.dto.FarmerProfileRequest;
import com.farmer_retailer.model.Farmer;
import com.farmer_retailer.model.User;
import com.farmer_retailer.repository.FarmerRepository;
import com.farmer_retailer.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FarmerService {

    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository;

    public FarmerService(FarmerRepository farmerRepository, UserRepository userRepository) {
        this.farmerRepository = farmerRepository;
        this.userRepository = userRepository;
    }

    // ==============================
    // Create or Update Farmer Profile
    // ==============================
    public Farmer createOrUpdateProfile(Long userId, FarmerProfileRequest req) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Farmer farmer = farmerRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    Farmer f = new Farmer();
                    f.setUser(user); // Link user
                    return f;
                });

        farmer.setFarmName(req.getFarmName());
        farmer.setPhone(req.getPhone());
        farmer.setDistrict(req.getDistrict());
        farmer.setProfileCompleted(true);

        return farmerRepository.save(farmer);
    }

    // ==============================
    // Get Farmer Profile by User ID
    // ==============================
    public Farmer getProfile(Long userId) {
        return farmerRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Farmer profile not found"));
    }
}
