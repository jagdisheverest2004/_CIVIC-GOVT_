package org.example.civic_govt.service;

import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.Community;
import org.example.civic_govt.repository.IssueRepository;
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
    private NotificationService notificationService; // To send notifications on status updates

    public Issue createIssue(Issue issue, User reporter, Community community) {
        issue.setReporter(reporter);
        issue.setCommunity(community);
        issue.setCreatedAt(LocalDateTime.now());
        issue.setUpdatedAt(LocalDateTime.now());
        // Set default values
        if (issue.getStatus() == null) {
            issue.setStatus(Issue.Status.PENDING);
        }
        if (issue.getPriority() == null) {
            issue.setPriority(Issue.Priority.LOW);
        }

        return issueRepository.save(issue);
    }

    public Issue updateIssueStatus(Long issueId, Issue.Status newStatus, User official) {
        return issueRepository.findById(issueId).map(issue -> {
            issue.setStatus(newStatus);
            issue.setUpdatedAt(LocalDateTime.now());
            Issue updatedIssue = issueRepository.save(issue);
            // Notify the reporter about the status change
            notificationService.createNotification(issue.getReporter(), "Your issue '" + issue.getTitle() + "' has been updated to " + newStatus.name());
            return updatedIssue;
        }).orElseThrow(() -> new RuntimeException("Issue not found with id " + issueId));
    }

    public Issue assignIssue(Long issueId, User assignee) {
        return issueRepository.findById(issueId).map(issue -> {
            issue.setAssignee(assignee);
            issue.setUpdatedAt(LocalDateTime.now());
            Issue updatedIssue = issueRepository.save(issue);
            // Notify the new assignee
            notificationService.createNotification(assignee, "You have been assigned to the issue '" + issue.getTitle() + "'");
            return updatedIssue;
        }).orElseThrow(() -> new RuntimeException("Issue not found with id " + issueId));
    }

    public List<Issue> findAllIssues() {
        return issueRepository.findAll();
    }

    public List<Issue> findIssuesByStatus(Issue.Status status) {
        return issueRepository.findByStatus(status);
    }

    public List<Issue> findIssuesByDepartment(Department department) {
        return issueRepository.findByDepartment(department);
    }

    public List<Issue> findReportedIssues(User reporter) {
        return issueRepository.findByReporter(reporter);
    }

    public Optional<Issue> findById(Long issueId) {
        return issueRepository.findById(issueId);
    }
}