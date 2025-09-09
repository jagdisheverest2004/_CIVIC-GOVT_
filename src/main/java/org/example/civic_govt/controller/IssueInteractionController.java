package org.example.civic_govt.controller;

import org.example.civic_govt.model.Comment;
import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Vote;
import org.example.civic_govt.service.CommentService;
import org.example.civic_govt.service.IssueService;
import org.example.civic_govt.service.UserService;
import org.example.civic_govt.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/issues/{issueId}")
public class IssueInteractionController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @PostMapping("/comments/{userId}")
    public ResponseEntity<Comment> addComment(@PathVariable Long issueId,
                                              @PathVariable Long userId,
                                              @RequestBody String text) {
        Optional<Issue> issue = issueService.findById(issueId);
        Optional<User> user = userService.findById(userId);

        if (issue.isPresent() && user.isPresent()) {
            Comment newComment = commentService.addComment(text, issue.get(), user.get());
            return new ResponseEntity<>(newComment, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/votes/{userId}")
    public ResponseEntity<Vote> voteForIssue(@PathVariable Long issueId, @PathVariable Long userId) {
        Optional<Issue> issue = issueService.findById(issueId);
        Optional<User> user = userService.findById(userId);

        if (issue.isPresent() && user.isPresent()) {
            try {
                Vote newVote = voteService.voteForIssue(issue.get(), user.get());
                return new ResponseEntity<>(newVote, HttpStatus.CREATED);
            } catch (IllegalStateException e) {
                return new ResponseEntity<>(HttpStatus.CONFLICT); // User already voted
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}