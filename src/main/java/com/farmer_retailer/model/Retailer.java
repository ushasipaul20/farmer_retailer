package com.farmer_retailer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "retailer")
public class Retailer {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Retailer() {}
    public Retailer(User user) { this.user = user; }

    public Long getUserId() { return userId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
