package org.example.civic_govt.repository;

import org.example.civic_govt.model.Notification;
import org.example.civic_govt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserAndIsRead(User user, boolean b);

    @Query("SELECT n FROM Notification n WHERE n.user.id = ?1 AND n.isRead = false")
    List<Notification> findByUserUserIdOrderByCreatedAtDesc(Long userId);
}

