//package com.farmer_retailer.controller;
//
//import com.farmer_retailer.model.FarmerVerification;
//import com.farmer_retailer.service.FarmerVerificationService;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/farmers/verification")
//@CrossOrigin(origins = "http://localhost:5173")
//public class FarmerVerificationController {
//
//    private final FarmerVerificationService service;
//
//    public FarmerVerificationController(FarmerVerificationService service) {
//        this.service = service;
//    }
//
//    @PostMapping("/upload")
//    public FarmerVerification upload(
//            @RequestParam Long userId,
//            @RequestParam MultipartFile aadhaar,
//            @RequestParam MultipartFile pan
//    ) throws Exception {
//        return service.uploadDocuments(userId, aadhaar, pan);
//    }
//}

//package com.farmer_retailer.controller;
//
//import com.farmer_retailer.model.FarmerVerification;
//import com.farmer_retailer.model.VerificationStatus;
//import com.farmer_retailer.service.FarmerVerificationService;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/farmers/verification")
//@CrossOrigin(origins = "http://localhost:5173")
//public class FarmerVerificationController {
//
//    private final FarmerVerificationService service;
//
//    public FarmerVerificationController(FarmerVerificationService service) {
//        this.service = service;
//    }
//
//    // 🔹 Upload Aadhaar and PAN documents
//    @PostMapping("/upload")
//    public FarmerVerification upload(
//            @RequestParam Long userId,
//            @RequestParam MultipartFile aadhaar,
//            @RequestParam MultipartFile pan
//    ) throws Exception {
//        return service.uploadDocuments(userId, aadhaar, pan);
//    }
//
//    // 🔹 New endpoint: Get verification status for a farmer
//    @GetMapping("/status/{userId}")
//    public VerificationStatus getStatus(@PathVariable Long userId) {
//        Optional<FarmerVerification> verificationOpt = service.getVerificationByUserId(userId);
//        return verificationOpt.map(FarmerVerification::getStatus)
//                .orElse(VerificationStatus.PENDING);
//    }
//
//
//}



package com.farmer_retailer.controller;

import com.farmer_retailer.model.FarmerVerification;
import com.farmer_retailer.model.VerificationStatus;
import com.farmer_retailer.service.EmailService;
import com.farmer_retailer.service.FarmerVerificationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/farmers/verification")
@CrossOrigin(origins = "http://localhost:5173")
public class FarmerVerificationController {

    private final FarmerVerificationService service;
    private final EmailService emailService;   // ✅ inject EmailService

    public FarmerVerificationController(
            FarmerVerificationService service,
            EmailService emailService
    ) {
        this.service = service;
        this.emailService = emailService;
    }

    // 🔹 Upload Aadhaar and PAN documents
    @PostMapping("/upload")
    public FarmerVerification upload(
            @RequestParam Long userId,
            @RequestParam MultipartFile aadhaar,
            @RequestParam MultipartFile pan
    ) throws Exception {
        return service.uploadDocuments(userId, aadhaar, pan);
    }

    // 🔹 Get verification status
    @GetMapping("/status/{userId}")
    public VerificationStatus getStatus(@PathVariable Long userId) {
        Optional<FarmerVerification> verificationOpt =
                service.getVerificationByUserId(userId);

        return verificationOpt
                .map(FarmerVerification::getStatus)
                .orElse(VerificationStatus.PENDING);
    }

    // 🔹 TEST EMAIL ENDPOINT
    @GetMapping("/test-mail")
    public String testMail() {
        emailService.sendEmail(
                "ushasipaul20@gmail.com",   // 👈 replace with your email
                "✅ Test Mail",
                "If you got this, email works 🎉"
        );
        return "Mail sent";
    }
}
