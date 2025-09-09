package org.example.civic_govt.controller;

import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Community;
import org.example.civic_govt.service.IssueService;
import org.example.civic_govt.service.UserService;
import org.example.civic_govt.service.CommunityService;
import org.example.civic_govt.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService; // To fetch reporter and assignee details

    @Autowired
    private CommunityService communityService;

    @Autowired
    private AuthUtil authUtil;

    // Endpoint for citizens to create a new issue
    @PostMapping("/create/{reporterId}/{communityId}")
    public ResponseEntity<?> createIssue(@RequestBody Issue issue,
                                             @PathVariable Long reporterId,
                                             @PathVariable Long communityId){
        try{
            Optional<User> reporter = userService.findById(reporterId);
            Optional<Community> community = communityService.findById(communityId);

            if (reporter.isPresent() && community.isPresent()) {
                Issue newIssue = issueService.createIssue(issue, reporter.get(), community.get());
                return new ResponseEntity<>(newIssue, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to create an issue.");
        }
    }

    // Endpoint for both citizens and officials to view all issues
    @GetMapping("/fetch-all")
    public ResponseEntity<List<Issue>> getAllIssues() {
        return new ResponseEntity<>(issueService.findAllIssues(), HttpStatus.OK);
    }

    // Endpoint for officials to assign an issue
    @PutMapping("/{issueId}/assign/{assigneeId}")
    public ResponseEntity<Issue> assignIssue(@PathVariable Long issueId, @PathVariable Long assigneeId) {
        Optional<User> assignee = userService.findById(assigneeId);
        if (assignee.isPresent()) {
            try {
                Issue updatedIssue = issueService.assignIssue(issueId, assignee.get());
                return new ResponseEntity<>(updatedIssue, HttpStatus.OK);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint for officials to update an issue's status
    @PutMapping("/{issueId}/status/{newStatus}")
    public ResponseEntity<Issue> updateIssueStatus(@PathVariable Long issueId, @PathVariable Issue.Status newStatus) {
        // Retrieve the official's ID from the security context
        User official = authUtil.getLoggedInUser();

        if(official == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(!official.getRole().equals(User.Role.OFFICIAL) ){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            Issue updatedIssue = issueService.updateIssueStatus(issueId, newStatus, official);
            return new ResponseEntity<>(updatedIssue, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}