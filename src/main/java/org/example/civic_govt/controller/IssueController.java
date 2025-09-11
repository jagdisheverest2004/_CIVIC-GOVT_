package org.example.civic_govt.controller;

import org.example.civic_govt.config.AppConstants;
import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.issues.CreateIssueDTO;
import org.example.civic_govt.payload.issues.FetchIssueDTO;
import org.example.civic_govt.payload.issues.FetchIssuesDTO;
import org.example.civic_govt.repository.DepartmentRepository;
import org.example.civic_govt.repository.IssueRepository;
import org.example.civic_govt.service.IssueService;
import org.example.civic_govt.service.UserService;
import org.example.civic_govt.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService; // To fetch reporter and assignee details


    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AuthUtil authUtil;

    @GetMapping("/departments/{departmentId}/issues")
    public ResponseEntity<?> getDefaultIssues(@PathVariable Long departmentId) {
        Optional<Department> department = departmentRepository.findById(departmentId);
        if (department.isPresent()) {
            return new ResponseEntity<>(department.get().getDefaultIssueTypes(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND);
    }

    // Endpoint for citizens to create a new issue
    @PostMapping("/create-issue")
    public ResponseEntity<?> createIssue(@RequestBody CreateIssueDTO createIssueDTO,@RequestParam MultipartFile[] images) {
        try{
            User reporterUser = authUtil.getLoggedInUser();
            Optional<User> reporter = userService.findById(reporterUser.getId());
            if(reporter.isEmpty()){
                throw new Exception();
            }
            FetchIssueDTO issueDTO = issueService.createIssue(createIssueDTO, reporter.get(),images);
            return ResponseEntity.status(HttpStatus.CREATED).body(issueDTO);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to create an issue.");
        }
    }

    // Endpoint for both citizens and officials to view all issues
    @GetMapping("/fetch-all")
    public ResponseEntity<?> getAllIssues(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false)  Integer pageSize,
            @RequestParam(name = "sortBy" , defaultValue = AppConstants.SORT_ISSUES_BY,required = false) String sortBy,
            @RequestParam(name = "sortOrder" , defaultValue = AppConstants.SORT_ISSUES_DIR,required = false) String sortOrder
    ) {
        FetchIssuesDTO issuesDTO = issueService.getAllIssues(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(issuesDTO);
    }

    // Endpoint for officials to assign an issue
    @PutMapping("/{issueId}/assign/{assigneeId}")
    public ResponseEntity<?> assignIssue(@PathVariable Long issueId, @PathVariable Long assigneeId) {

        try {
            User loggedInUser = authUtil.getLoggedInUser();

            if (loggedInUser == null) {
                throw new RuntimeException("User not logged in.");
            }

            if (!loggedInUser.getRole().equals(User.Role.ZONE_HEAD)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to assign issues.");
            }

            Boolean checkHead = issueRepository.existsIssueByIdAndHeadId(issueId, loggedInUser.getId());
            if (!checkHead) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to assign issues outside your jurisdiction.");
            }
            issueService.assignIssue(assigneeId, issueId);
            return ResponseEntity.status(HttpStatus.OK).body("Issue assigned successfully.");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // Endpoint for officials to update an issue's status
    @PutMapping("/{issueId}/status/{newStatus}")
    public ResponseEntity<?> updateIssueStatus(@PathVariable Long issueId, @PathVariable Issue.Status newStatus) {
        // Retrieve the official's ID from the security context
        User loggedInUser = authUtil.getLoggedInUser();

        if(loggedInUser == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(!loggedInUser.getRole().equals(User.Role.SUBORDINATE) ){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            issueService.updateIssueStatus(issueId, newStatus,loggedInUser);
            return new ResponseEntity<>("Issue status updated successfully.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}