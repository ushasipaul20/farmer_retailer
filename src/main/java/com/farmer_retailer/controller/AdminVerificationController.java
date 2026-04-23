//package com.farmer_retailer.controller;
//
//import com.farmer_retailer.model.FarmerVerification;
//import com.farmer_retailer.model.VerificationStatus;
//import com.farmer_retailer.repository.FarmerVerificationRepository;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/admin/verification")
//@CrossOrigin(origins = "http://localhost:5173")
//public class AdminVerificationController {
//
//    private final FarmerVerificationRepository verificationRepository;
//
//    public AdminVerificationController(FarmerVerificationRepository verificationRepository) {
//        this.verificationRepository = verificationRepository;
//    }
//
//    // 📌 GET ALL PENDING VERIFICATIONS
//    @GetMapping("/pending")
//    public List<FarmerVerification> pending() {
//        return verificationRepository.findAll()
//                .stream()
//                .filter(v -> v.getStatus() == VerificationStatus.PENDING)
//                .toList();
//    }
//
//    // ✅ APPROVE FARMER
//    @PutMapping("/approve/{userId}")
//    public ResponseEntity<String> approve(@PathVariable Long userId) {
//        Optional<FarmerVerification> optionalVerification =
//                verificationRepository.findByFarmer_UserId(userId);
//
//        if (optionalVerification.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        FarmerVerification verification = optionalVerification.get();
//        verification.setStatus(VerificationStatus.VERIFIED);
//        verificationRepository.save(verification);
//
//        return ResponseEntity.ok("Farmer approved successfully");
//    }
//
//    // ❌ REJECT FARMER
//    @PutMapping("/reject/{userId}")
//    public ResponseEntity<String> reject(@PathVariable Long userId) {
//        Optional<FarmerVerification> optionalVerification =
//                verificationRepository.findByFarmer_UserId(userId);
//
//        if (optionalVerification.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        FarmerVerification verification = optionalVerification.get();
//        verification.setStatus(VerificationStatus.REJECTED);
//        verificationRepository.save(verification);
//
//        return ResponseEntity.ok("Farmer rejected successfully");
//    }
//}




package com.farmer_retailer.controller;

import com.farmer_retailer.model.FarmerVerification;
import com.farmer_retailer.model.VerificationStatus;
import com.farmer_retailer.repository.FarmerVerificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/verification")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminVerificationController {

    private final FarmerVerificationRepository verificationRepository;

    public AdminVerificationController(FarmerVerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    // 📌 GET ALL PENDING VERIFICATIONS (with farmer name & email)
    @GetMapping("/pending")
    public List<Map<String, Object>> pending() {
        return verificationRepository.findAll()
                .stream()
                .filter(v -> v.getStatus() == VerificationStatus.PENDING)
                .map(v -> Map.of(
                        "id", v.getId(),
                        "aadhaarUrl", v.getAadhaarUrl(),
                        "panUrl", v.getPanUrl(),
                        "status", v.getStatus(),
                        "farmer", Map.of(
                                "userId", v.getFarmer().getUserId(),
                                "name", v.getFarmer().getName(),   // ✅ include name
                                "email", v.getFarmer().getEmail(), // ✅ include email
                                "farmName", v.getFarmer().getFarmName()
                        )
                ))
                .collect(Collectors.toList());
    }

    // ✅ APPROVE FARMER
    @PutMapping("/approve/{userId}")
    public ResponseEntity<String> approve(@PathVariable Long userId) {
        Optional<FarmerVerification> optionalVerification =
                verificationRepository.findByFarmer_UserId(userId);

        if (optionalVerification.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FarmerVerification verification = optionalVerification.get();
        verification.setStatus(VerificationStatus.VERIFIED);
        verificationRepository.save(verification);

        return ResponseEntity.ok("Farmer approved successfully");
    }

    // ❌ REJECT FARMER
    @PutMapping("/reject/{userId}")
    public ResponseEntity<String> reject(@PathVariable Long userId) {
        Optional<FarmerVerification> optionalVerification =
                verificationRepository.findByFarmer_UserId(userId);

        if (optionalVerification.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FarmerVerification verification = optionalVerification.get();
        verification.setStatus(VerificationStatus.REJECTED);
        verificationRepository.save(verification);

        return ResponseEntity.ok("Farmer rejected successfully");
    }
    // 📌 GET ALL VERIFICATIONS (pending + verified + rejected)
    @GetMapping("/all")
    public List<Map<String, Object>> allVerifications() {
        return verificationRepository.findAll()
                .stream()
                .map(v -> Map.of(
                        "id", v.getId(),
                        "aadhaarUrl", v.getAadhaarUrl(),
                        "panUrl", v.getPanUrl(),
                        "status", v.getStatus(),
                        "farmer", Map.of(
                                "userId", v.getFarmer().getUserId(),
                                "name", v.getFarmer().getName(),
                                "email", v.getFarmer().getEmail(),
                                "farmName", v.getFarmer().getFarmName()
                        )
                ))
                .collect(Collectors.toList());
    }

}
