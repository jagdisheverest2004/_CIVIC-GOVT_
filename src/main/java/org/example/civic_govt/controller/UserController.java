package org.example.civic_govt.controller;

import org.example.civic_govt.config.AppConstants;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.issues.FetchIssueDTO;
import org.example.civic_govt.payload.issues.FetchIssuesDTO;
import org.example.civic_govt.payload.issues.IssueFilterDTO;
import org.example.civic_govt.payload.users.FetchCitizenDTO;
import org.example.civic_govt.service.IssueService;
import org.example.civic_govt.service.UserService;
import org.example.civic_govt.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/users")
public class UserController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private UserService userService;

    // Endpoint for citizens to view their reported issues
    @GetMapping("/issues/reported")
    public ResponseEntity<?> getReportedIssues(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ISSUES_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ISSUES_DIR) String sortOrder,
            @RequestBody(required = false) IssueFilterDTO filters) {

        User loggedInUser = authUtil.getLoggedInUser();
        if (!loggedInUser.getRole().equals(User.Role.CITIZEN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }

        FetchIssuesDTO issues = issueService.getReportedIssues(loggedInUser.getId(), pageNumber, pageSize, sortBy, sortOrder, filters);
        return ResponseEntity.ok(issues);
    }

    // Endpoint for subordinates to view their assigned issues
    @GetMapping("/issues/assigned")
    public ResponseEntity<?> getAssignedIssues(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ISSUES_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ISSUES_DIR) String sortOrder,
            @RequestBody(required = false) IssueFilterDTO filters) {

        User loggedInUser = authUtil.getLoggedInUser();
        if (!loggedInUser.getRole().equals(User.Role.SUBORDINATE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }

        FetchIssuesDTO issues = issueService.getAssignedIssues(loggedInUser.getId(), pageNumber, pageSize, sortBy, sortOrder, filters);
        return ResponseEntity.ok(issues);
    }

    // Endpoint for all authenticated users to view details of a single issue
    @GetMapping("/issues/{issueId}")
    public ResponseEntity<?> getIssueDetails(@PathVariable Long issueId) {
        User loggedInUser = authUtil.getLoggedInUser();
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        try {
            FetchIssueDTO issueDetails = issueService.getSingleIssueDetails(issueId);
            return ResponseEntity.ok(issueDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUserDetails() {
        User loggedInUser = authUtil.getLoggedInUser();
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }
        if(loggedInUser.getRole().equals(User.Role.CITIZEN)){
            FetchCitizenDTO citizenDTO = userService.createCitizenDTO(loggedInUser);
            return ResponseEntity.ok(citizenDTO);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
    }
}