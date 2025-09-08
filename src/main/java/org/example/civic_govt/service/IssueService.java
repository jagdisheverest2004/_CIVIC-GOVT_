package org.example.civic_govt.service;

import org.example.civic_govt.model.*;
import org.example.civic_govt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IssueService {
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private UserRepository userRepository;

    public Issue reportIssue(String title, String description, String category, Double lat, Double lng, String priority, String mediaUrl, Long reporterId) {
        User reporter = userRepository.findById(reporterId).orElseThrow();
        Issue issue = Issue.builder()
                .title(title)
                .description(description)
                .category(category)
                .locationLat(lat)
                .locationLong(lng)
                .priority(priority)
                .status(Issue.Status.PENDING)
                .reporter(reporter)
                .mediaUrl(mediaUrl)
                .createdAt(LocalDateTime.now())
                .build();
        return issueRepository.save(issue);
    }

    public List<Issue> listIssues() {
        return issueRepository.findAll();
    }

    public Optional<Issue> getIssue(Long id) {
        return issueRepository.findById(id);
    }

    public Issue updateStatus(Long id, Issue.Status status) {
        Issue issue = issueRepository.findById(id).orElseThrow();
        issue.setStatus(status);
        return issueRepository.save(issue);
    }

    public Issue assignIssue(Long id, Long assigneeId) {
        Issue issue = issueRepository.findById(id).orElseThrow();
        User assignee = userRepository.findById(assigneeId).orElseThrow();
        issue.setAssignee(assignee);
        return issueRepository.save(issue);
    }

    public void deleteIssue(Long id) {
        issueRepository.deleteById(id);
    }
}

