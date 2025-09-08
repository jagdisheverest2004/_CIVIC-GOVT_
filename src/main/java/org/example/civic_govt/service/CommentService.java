package org.example.civic_govt.service;

import org.example.civic_govt.model.*;
import org.example.civic_govt.repository.*;
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
    private UserRepository userRepository;

    public Comment addComment(Long issueId, Long userId, String text) {
        Issue issue = issueRepository.findById(issueId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Comment comment = Comment.builder()
                .issue(issue)
                .user(user)
                .text(text)
                .createdAt(LocalDateTime.now())
                .build();
        return commentRepository.save(comment);
    }

    public List<Comment> getComments(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow();
        return commentRepository.findByIssue(issue);
    }
}

