package org.example.civic_govt.service;

import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Vote;
import org.example.civic_govt.repository.IssueRepository;
import org.example.civic_govt.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private NotificationService notificationService;

    public Vote voteForIssue(Issue issue, User user) {
        // Check if the user has already voted on this issue
        Optional<Vote> existingVote = voteRepository.findByIssueAndUser(issue, user);
        if (existingVote.isPresent()) {
            throw new IllegalStateException("User has already voted on this issue.");
        }

        Vote vote = new Vote();
        vote.setIssue(issue);
        vote.setUser(user);
        vote.setCreatedAt(LocalDateTime.now());

        List<User> reporters = issueRepository.findReportersByIssueId(issue.getId());
        for (User reporter : reporters) {
            if (!reporter.getId().equals(user.getId())) { // Avoid notifying the commenter themselves
                notificationService.createNotification(reporter, "New vote on issue you reported: " + issue.getTitle());
            }
        }

        return voteRepository.save(vote);
    }
}