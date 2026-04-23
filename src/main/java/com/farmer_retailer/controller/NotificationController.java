package com.farmer_retailer.controller;

import com.farmer_retailer.model.Notification;
import com.farmer_retailer.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepo;

    // ✅ Fetch notifications for ANY user (Farmer / Retailer)
    @GetMapping("/{userId}")
    public List<Notification> getNotifications(@PathVariable Long userId) {
        return notificationRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
