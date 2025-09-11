package org.example.civic_govt.service;

import org.example.civic_govt.model.Notification;
import org.example.civic_govt.model.User;
import org.example.civic_govt.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotificationsForUser(User user) {
        return notificationRepository.findByUserAndIsRead(user, false);
    }

    public Notification markAsRead(Long notificationId) {
        return notificationRepository.findById(notificationId).map(notification -> {
            notification.setIsRead(true);
            return notificationRepository.save(notification);
        }).orElseThrow(() -> new RuntimeException("Notification not found with id " + notificationId));
    }

    public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findByUserUserIdOrderByCreatedAtDesc(userId);
    }
}