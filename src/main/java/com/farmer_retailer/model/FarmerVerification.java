//package com.farmer_retailer.model;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "farmer_verification")
//public class FarmerVerification {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne
//    @JoinColumn(name = "farmer_id", nullable = false, unique = true)
//    private Farmer farmer;
//
//    @Column(nullable = false)
//    private String aadhaarUrl;
//
//    @Column(nullable = false)
//    private String panUrl;
//
//    @Enumerated(EnumType.STRING)
//    private VerificationStatus status = VerificationStatus.PENDING;
//
//    private LocalDateTime submittedAt;
//    private LocalDateTime verifiedAt;
//
//    public FarmerVerification() {}
//
//    public FarmerVerification(Farmer farmer, String aadhaarUrl, String panUrl) {
//        this.farmer = farmer;
//        this.aadhaarUrl = aadhaarUrl;
//        this.panUrl = panUrl;
//        this.submittedAt = LocalDateTime.now();
//    }
//
//    public Long getId() { return id; }
//
//    public Farmer getFarmer() { return farmer; }
//    public void setFarmer(Farmer farmer) { this.farmer = farmer; }
//
//    public String getAadhaarUrl() { return aadhaarUrl; }
//    public void setAadhaarUrl(String aadhaarUrl) { this.aadhaarUrl = aadhaarUrl; }
//
//    public String getPanUrl() { return panUrl; }
//    public void setPanUrl(String panUrl) { this.panUrl = panUrl; }
//
//    public VerificationStatus getStatus() { return status; }
//    public void setStatus(VerificationStatus status) {
//        this.status = status;
//        if (status == VerificationStatus.VERIFIED) {
//            this.verifiedAt = LocalDateTime.now();
//        }
//    }
//
//    public LocalDateTime getSubmittedAt() { return submittedAt; }
//    public LocalDateTime getVerifiedAt() { return verifiedAt; }
//}



package com.farmer_retailer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "farmer_verification")
public class FarmerVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "farmer_id", nullable = false, unique = true)
    private Farmer farmer;

    private String aadhaarUrl;
    private String panUrl;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status = VerificationStatus.PENDING;

    private LocalDateTime submittedAt = LocalDateTime.now();
    private LocalDateTime verifiedAt;

    // getters & setters
    public Long getId() { return id; }
    public Farmer getFarmer() { return farmer; }
    public void setFarmer(Farmer farmer) { this.farmer = farmer; }
    public String getAadhaarUrl() { return aadhaarUrl; }
    public void setAadhaarUrl(String aadhaarUrl) { this.aadhaarUrl = aadhaarUrl; }
    public String getPanUrl() { return panUrl; }
    public void setPanUrl(String panUrl) { this.panUrl = panUrl; }

    public VerificationStatus getStatus() { return status; }
    public void setStatus(VerificationStatus status) {
        this.status = status;
        if (status == VerificationStatus.VERIFIED) {
            this.verifiedAt = LocalDateTime.now();
        }
    }
}
