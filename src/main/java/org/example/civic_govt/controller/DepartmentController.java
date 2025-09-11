package org.example.civic_govt.controller;

import org.example.civic_govt.config.AppConstants;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.issues.FetchIssuesDTO;
import org.example.civic_govt.payload.issues.IssueFilterDTO;
import org.example.civic_govt.payload.users.FetchUsersDTO;
import org.example.civic_govt.service.DepartmentService;
import org.example.civic_govt.service.IssueService;
import org.example.civic_govt.service.UserService;
import org.example.civic_govt.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth/departments")
public class DepartmentController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/issues")
    public ResponseEntity<?> getDepartmentIssues(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ISSUES_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ISSUES_DIR) String sortOrder,
            @RequestBody(required = false) IssueFilterDTO filters) {

        User deptHead = authUtil.getLoggedInUser();
        if (!deptHead.getRole().equals(User.Role.DEPT_HEAD) || deptHead.getDepartment() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }

        if (filters == null) {
            filters = new IssueFilterDTO();
        }

        FetchIssuesDTO issuesDTO = issueService.getIssuesWithFilters(pageNumber, pageSize, sortBy, sortOrder, filters);
        return ResponseEntity.ok(issuesDTO);
    }

    @GetMapping("/officials")
    public ResponseEntity<?> getDepartmentOfficials(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_USERS_DIR) String sortOrder
            ) {
        User deptHead = authUtil.getLoggedInUser();
        if (!deptHead.getRole().equals(User.Role.DEPT_HEAD) || deptHead.getDepartment() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }
        FetchUsersDTO officials = departmentService.findDistrictOfficialsByDepartment(deptHead.getDepartment().getId(), pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(officials);
    }
}