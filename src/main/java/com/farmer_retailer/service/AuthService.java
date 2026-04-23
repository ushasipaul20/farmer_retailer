
package com.farmer_retailer.service;

import com.farmer_retailer.model.*;
import com.farmer_retailer.repository.FarmerRepository;
import com.farmer_retailer.repository.RetailerRepository;
import com.farmer_retailer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ===========================
    // REGISTER USER
    // ===========================
    @Transactional
    public User register(String name, String email, String password, Role role) {

        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Create new User
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        // Save User first
        User savedUser = userRepository.save(user);

        // Handle FARMER role
        if (role == Role.FARMER) {
            Farmer farmer = new Farmer();
            farmer.setUser(savedUser);       // MapsId ensures user_id linkage
            farmer.setFarmName("");          // Optional default
            farmer.setProfileCompleted(false);
            savedUser.setFarmer(farmer);
            farmerRepository.save(farmer);   // Save Farmer explicitly
        }

        // Handle RETAILER role
        if (role == Role.RETAILER) {
            Retailer retailer = new Retailer();
            retailer.setUser(savedUser);
            savedUser.setRetailer(retailer);
            retailerRepository.save(retailer); // Save Retailer explicitly
        }

        return savedUser;
    }

    // ===========================
    // LOGIN USER
    // ===========================
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}
