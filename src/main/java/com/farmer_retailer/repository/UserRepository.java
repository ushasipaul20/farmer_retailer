package com.farmer_retailer.repository;

import com.farmer_retailer.model.Role;
import com.farmer_retailer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔍 Find user by email
    Optional<User> findByEmail(String email);

    // ✅ Find all users with a specific role (for admin dashboard)
    List<User> findByRole(Role role);
}
