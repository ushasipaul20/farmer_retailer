package com.farmer_retailer.service;

import com.farmer_retailer.model.Notification;
import com.farmer_retailer.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;

    public void notify(Long userId, String message, String type) {
        System.out.println("🔔 NOTIFICATION SAVED for userId = " + userId);

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setType(type);
        notificationRepo.save(notification);
    }
}
