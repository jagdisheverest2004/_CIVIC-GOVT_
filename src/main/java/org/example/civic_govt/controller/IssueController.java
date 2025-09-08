package org.example.civic_govt.controller;

import org.example.civic_govt.dto.IssueRequest;
import org.example.civic_govt.dto.AssignIssueRequest;
import org.example.civic_govt.model.Issue;
import org.example.civic_govt.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {
    @Autowired
    private IssueService issueService;

    @PostMapping
    public ResponseEntity<Issue> reportIssue(@Valid @RequestBody IssueRequest request, @RequestParam Long reporterId) {
        Issue issue = issueService.reportIssue(request.getTitle(), request.getDescription(), request.getCategory(), request.getLocationLat(), request.getLocationLong(), request.getPriority(), request.getMediaUrl(), reporterId);
        return ResponseEntity.ok(issue);
    }

    @GetMapping
    public ResponseEntity<List<Issue>> listIssues() {
        return ResponseEntity.ok(issueService.listIssues());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssue(@PathVariable Long id) {
        return issueService.getIssue(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Issue> updateStatus(@PathVariable Long id, @RequestParam Issue.Status status) {
        return ResponseEntity.ok(issueService.updateStatus(id, status));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<Issue> assignIssue(@PathVariable Long id, @Valid @RequestBody AssignIssueRequest request) {
        return ResponseEntity.ok(issueService.assignIssue(id, request.getAssigneeId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIssue(@PathVariable Long id) {
        issueService.deleteIssue(id);
        return ResponseEntity.ok().build();
    }
}

