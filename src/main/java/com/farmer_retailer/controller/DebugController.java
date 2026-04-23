package com.farmer_retailer.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @GetMapping("/me")
    public Object me(Authentication auth) {
        return auth.getAuthorities();
    }
}
