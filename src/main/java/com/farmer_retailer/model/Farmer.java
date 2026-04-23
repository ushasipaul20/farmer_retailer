//package com.farmer_retailer.model;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "farmers")
//public class Farmer {
//
//    @Id
//    @Column(name = "user_id")
//    private Long userId;
//
//    @OneToOne
//    @MapsId
//    @JoinColumn(name = "user_id")
//    @JsonIgnore
//    private User user;
//
//    private String farmName;
//    private String phone;
//    private String district;
//    private boolean profileCompleted = false;
//
//    private String aadhaarUrl;
//    private String panUrl;
//
//    @Enumerated(EnumType.STRING)
//    private VerificationStatus verificationStatus = VerificationStatus.PENDING;
//
//    public Farmer() {}
//    public Farmer(User user) { this.user = user; }
//
//    public Long getUserId() { return userId; }
//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }
//
//    public String getFarmName() { return farmName; }
//    public void setFarmName(String farmName) { this.farmName = farmName; }
//    public String getPhone() { return phone; }
//    public void setPhone(String phone) { this.phone = phone; }
//    public String getDistrict() { return district; }
//    public void setDistrict(String district) { this.district = district; }
//    public boolean isProfileCompleted() { return profileCompleted; }
//    public void setProfileCompleted(boolean profileCompleted) { this.profileCompleted = profileCompleted; }
//    public String getAadhaarUrl() { return aadhaarUrl; }
//    public void setAadhaarUrl(String aadhaarUrl) { this.aadhaarUrl = aadhaarUrl; }
//
//    public String getPanUrl() { return panUrl; }
//    public void setPanUrl(String panUrl) { this.panUrl = panUrl; }
//
//    public VerificationStatus getVerificationStatus() {
//        return verificationStatus;
//    }
//
//    public void setVerificationStatus(VerificationStatus verificationStatus) {
//        this.verificationStatus = verificationStatus;
//    }
//}


package com.farmer_retailer.model;

import jakarta.persistence.*;

@Entity
@Table(name = "farmers")
public class Farmer {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;  // removed @JsonIgnore

    private String farmName;
    private String phone;
    private String district;
    private boolean profileCompleted = false;

    private String aadhaarUrl;
    private String panUrl;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    public Farmer() {}

    public Farmer(User user) { this.user = user; }

    public Long getUserId() { return userId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFarmName() { return farmName; }
    public void setFarmName(String farmName) { this.farmName = farmName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public boolean isProfileCompleted() { return profileCompleted; }
    public void setProfileCompleted(boolean profileCompleted) { this.profileCompleted = profileCompleted; }

    public String getAadhaarUrl() { return aadhaarUrl; }
    public void setAadhaarUrl(String aadhaarUrl) { this.aadhaarUrl = aadhaarUrl; }

    public String getPanUrl() { return panUrl; }
    public void setPanUrl(String panUrl) { this.panUrl = panUrl; }

    public VerificationStatus getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(VerificationStatus verificationStatus) { this.verificationStatus = verificationStatus; }

    // ✅ Add JSON getters for name and email
    @Transient
    public String getName() {
        return user != null ? user.getName() : null;
    }

    @Transient
    public String getEmail() {
        return user != null ? user.getEmail() : null;
    }
}
