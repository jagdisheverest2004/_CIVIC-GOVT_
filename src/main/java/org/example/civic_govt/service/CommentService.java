package org.example.civic_govt.service;

import org.example.civic_govt.model.Comment;
import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.example.civic_govt.repository.CommentRepository;
import org.example.civic_govt.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IssueRepository issueRepository;

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
        List<User> reporters = issueRepository.findReportersByIssueId(issue.getId());
        for (User reporter : reporters) {
            if (!reporter.getId().equals(user.getId())) { // Avoid notifying the commenter themselves
                notificationService.createNotification(reporter, "New comment on issue " + issue.getTitle());
            }
        }
        return savedComment;
    }
}