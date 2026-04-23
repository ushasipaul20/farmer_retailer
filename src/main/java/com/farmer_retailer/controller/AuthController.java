package com.farmer_retailer.controller;

import com.farmer_retailer.dto.LoginRequest;
import com.farmer_retailer.dto.LoginResponseDTO;
import com.farmer_retailer.dto.RegisterRequest;
import com.farmer_retailer.model.Role;
import com.farmer_retailer.model.User;
import com.farmer_retailer.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {



    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {

        Role role = Role.valueOf(request.getRole().toUpperCase());

        User user = authService.register(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                role
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequest request) {

        User user = authService.login(request.getEmail(), request.getPassword());

        LoginResponseDTO response = new LoginResponseDTO(
                user.getEmail(),
                user.getRole().name(),
                String.valueOf(user.getId())
        );

        return ResponseEntity.ok(response);
    }
}
