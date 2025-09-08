package org.example.civic_govt.service;

import org.example.civic_govt.model.*;
import org.example.civic_govt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Notification> getNotifications(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return notificationRepository.findByUser(user);
    }

    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow();
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    public Notification createNotification(Long userId, String message) {
        User user = userRepository.findById(userId).orElseThrow();
        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        return notificationRepository.save(notification);
    }
}

