package org.example.civic_govt.controller;

import org.example.civic_govt.model.Notification;
import org.example.civic_govt.model.User;
import org.example.civic_govt.service.NotificationService;
import org.example.civic_govt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/users/{userId}/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            List<Notification> notifications = notificationService.getUnreadNotificationsForUser(user.get());
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long notificationId) {
        try {
            Notification notification = notificationService.markAsRead(notificationId);
            return new ResponseEntity<>(notification, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}