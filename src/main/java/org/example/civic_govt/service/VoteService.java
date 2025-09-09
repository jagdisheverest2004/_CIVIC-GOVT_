package org.example.civic_govt.service;

import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Vote;
import org.example.civic_govt.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

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
        return voteRepository.save(vote);
    }
}