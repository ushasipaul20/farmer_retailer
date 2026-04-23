//package com.farmer_retailer.service;
//
//import com.farmer_retailer.model.*;
//import com.farmer_retailer.repository.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class FarmerVerificationService {
//
//    private final FarmerRepository farmerRepository;
//    private final FarmerVerificationRepository verificationRepository;
//
//    private static final String DIR = System.getProperty("user.dir") + "/uploads/verification/";
//
//    public FarmerVerificationService(
//            FarmerRepository farmerRepository,
//            FarmerVerificationRepository verificationRepository
//    ) {
//        this.farmerRepository = farmerRepository;
//        this.verificationRepository = verificationRepository;
//    }
//
//    // 📤 Upload Aadhaar + PAN
//    public FarmerVerification uploadDocuments(Long userId,
//                                              MultipartFile aadhaar,
//                                              MultipartFile pan) throws Exception {
//
//        Farmer farmer = farmerRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Farmer not found"));
//
//        new File(DIR).mkdirs();
//
//        String aadhaarFile = UUID.randomUUID() + "_" + aadhaar.getOriginalFilename();
//        String panFile = UUID.randomUUID() + "_" + pan.getOriginalFilename();
//
//        aadhaar.transferTo(new File(DIR + aadhaarFile));
//        pan.transferTo(new File(DIR + panFile));
//
//        FarmerVerification verification = verificationRepository
//                .findByFarmer_UserId(userId)
//                .orElse(new FarmerVerification());
//
//        verification.setFarmer(farmer);
//        verification.setAadhaarUrl("/uploads/verification/" + aadhaarFile);
//        verification.setPanUrl("/uploads/verification/" + panFile);
//        verification.setStatus(VerificationStatus.PENDING);
//
//        return verificationRepository.save(verification);
//    }
//
//    // ✅ Admin approve
//    public FarmerVerification approve(Long userId) {
//        FarmerVerification v = verificationRepository
//                .findByFarmer_UserId(userId)
//                .orElseThrow(() -> new RuntimeException("Verification not found"));
//        v.setStatus(VerificationStatus.VERIFIED);
//        return verificationRepository.save(v);
//    }
//
//    // ❌ Admin reject
//    public FarmerVerification reject(Long userId) {
//        FarmerVerification v = verificationRepository
//                .findByFarmer_UserId(userId)
//                .orElseThrow(() -> new RuntimeException("Verification not found"));
//        v.setStatus(VerificationStatus.REJECTED);
//        return verificationRepository.save(v);
//    }
//
//    // 🔒 Check if farmer is verified
//    public boolean isFarmerVerified(Long userId) {
//        return verificationRepository.findByFarmer_UserId(userId)
//                .map(v -> v.getStatus() == VerificationStatus.VERIFIED)
//                .orElse(false);
//    }
//
//    // 🔹 New method to fetch verification by userId (used by /status endpoint)
//    public Optional<FarmerVerification> getVerificationByUserId(Long userId) {
//        return verificationRepository.findByFarmer_UserId(userId);
//    }
//}


package com.farmer_retailer.service;

import com.farmer_retailer.model.*;
import com.farmer_retailer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

@Service
public class FarmerVerificationService {

    private final FarmerRepository farmerRepository;
    private final FarmerVerificationRepository verificationRepository;

    @Autowired
    private EmailService emailService;

    private static final String DIR =
            System.getProperty("user.dir") + "/uploads/verification/";

    public FarmerVerificationService(
            FarmerRepository farmerRepository,
            FarmerVerificationRepository verificationRepository
    ) {
        this.farmerRepository = farmerRepository;
        this.verificationRepository = verificationRepository;
    }

    // 📤 Upload Aadhaar + PAN
    public FarmerVerification uploadDocuments(
            Long userId,
            MultipartFile aadhaar,
            MultipartFile pan
    ) throws Exception {

        Farmer farmer = farmerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        new File(DIR).mkdirs();

        String aadhaarFile = UUID.randomUUID() + "_" + aadhaar.getOriginalFilename();
        String panFile = UUID.randomUUID() + "_" + pan.getOriginalFilename();

        aadhaar.transferTo(new File(DIR + aadhaarFile));
        pan.transferTo(new File(DIR + panFile));

        FarmerVerification verification = verificationRepository
                .findByFarmer_UserId(userId)
                .orElse(new FarmerVerification());

        verification.setFarmer(farmer);
        verification.setAadhaarUrl("/uploads/verification/" + aadhaarFile);
        verification.setPanUrl("/uploads/verification/" + panFile);
        verification.setStatus(VerificationStatus.PENDING);

        return verificationRepository.save(verification);
    }

    // ✅ Admin approve → SEND EMAIL
    public FarmerVerification approve(Long userId) {

        FarmerVerification v = verificationRepository
                .findByFarmer_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Verification not found"));

        v.setStatus(VerificationStatus.VERIFIED);
        FarmerVerification saved = verificationRepository.save(v);

        // 📧 Email to farmer
        String email = v.getFarmer().getUser().getEmail();
        String name = v.getFarmer().getUser().getName();

        emailService.sendEmail(
                email,
                "✅ Farmer Verification Approved",
                "Hello " + name + ",\n\n" +
                        "Your farmer profile has been successfully VERIFIED.\n" +
                        "You can now add products and start selling.\n\n" +
                        "– FarmConnect Team"
        );

        return saved;
    }

    // ❌ Admin reject → SEND EMAIL
    public FarmerVerification reject(Long userId) {

        FarmerVerification v = verificationRepository
                .findByFarmer_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Verification not found"));

        v.setStatus(VerificationStatus.REJECTED);
        FarmerVerification saved = verificationRepository.save(v);

        // 📧 Email to farmer
        String email = v.getFarmer().getUser().getEmail();
        String name = v.getFarmer().getUser().getName();

        emailService.sendEmail(
                email,
                "❌ Farmer Verification Rejected",
                "Hello " + name + ",\n\n" +
                        "Your farmer verification request has been REJECTED.\n" +
                        "Please re-upload valid Aadhaar and PAN documents.\n\n" +
                        "– FarmConnect Team"
        );

        return saved;
    }

    // 🔒 Check if farmer is verified (used by ProductService)
    public boolean isFarmerVerified(Long userId) {
        return verificationRepository.findByFarmer_UserId(userId)
                .map(v -> v.getStatus() == VerificationStatus.VERIFIED)
                .orElse(false);
    }

    // 🔹 Used by /status endpoint
    public Optional<FarmerVerification> getVerificationByUserId(Long userId) {
        return verificationRepository.findByFarmer_UserId(userId);
    }
}
