package org.example.civic_govt.repository;

import org.example.civic_govt.model.Notification;
import org.example.civic_govt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
}

