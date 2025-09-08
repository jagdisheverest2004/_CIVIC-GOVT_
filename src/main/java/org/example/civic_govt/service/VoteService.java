package org.example.civic_govt.service;

import org.example.civic_govt.model.*;
import org.example.civic_govt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private UserRepository userRepository;

    public Vote upvote(Long issueId, Long userId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Vote vote = Vote.builder()
                .issue(issue)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        return voteRepository.save(vote);
    }

    public long getVoteCount(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow();
        return voteRepository.countByIssue(issue);
    }
}

