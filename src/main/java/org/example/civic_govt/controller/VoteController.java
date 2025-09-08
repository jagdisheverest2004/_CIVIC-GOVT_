package org.example.civic_govt.controller;

import org.example.civic_govt.model.Vote;
import org.example.civic_govt.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues/{issueId}/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;

    @PostMapping
    public ResponseEntity<Vote> upvote(@PathVariable Long issueId, @RequestParam Long userId) {
        Vote vote = voteService.upvote(issueId, userId);
        return ResponseEntity.ok(vote);
    }

    @GetMapping
    public ResponseEntity<Long> getVoteCount(@PathVariable Long issueId) {
        long count = voteService.getVoteCount(issueId);
        return ResponseEntity.ok(count);
    }
}

