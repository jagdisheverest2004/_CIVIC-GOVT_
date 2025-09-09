package org.example.civic_govt.service;

import org.example.civic_govt.model.Comment;
import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.example.civic_govt.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationService notificationService;

    public Comment addComment(String text, Issue issue, User user) {
        Comment comment = new Comment();
        comment.setText(text);
        comment.setIssue(issue);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        // Notify the issue reporter about the new comment
        if (!issue.getReporter().equals(user)) {
            notificationService.createNotification(issue.getReporter(), "A new comment was added to your issue '" + issue.getTitle() + "'");
        }
        return savedComment;
    }
}