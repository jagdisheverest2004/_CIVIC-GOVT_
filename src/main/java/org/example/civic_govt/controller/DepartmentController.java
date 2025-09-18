package org.example.civic_govt.controller;

import org.example.civic_govt.config.AppConstants;
import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.districts.FetchDistrictsDTO;
import org.example.civic_govt.payload.issues.FetchIssuesDTO;
import org.example.civic_govt.payload.issues.IssueFilterDTO;
import org.example.civic_govt.payload.users.FetchUserDTO;
import org.example.civic_govt.payload.users.FetchUsersDTO;
import org.example.civic_govt.service.DepartmentService;
import org.example.civic_govt.service.DistrictService;
import org.example.civic_govt.service.IssueService;
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
    private DistrictService districtService;

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

    @GetMapping("/districts")
    public ResponseEntity<?> getDepartmentDistricts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_DISTRICTS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DISTRICTS_DIR) String sortOrder,
            @RequestParam(name = "keywords", required = false) String keyword
    ) {
        User deptHead = authUtil.getLoggedInUser();
        if (!deptHead.getRole().equals(User.Role.DEPT_HEAD) || deptHead.getDepartment() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }
        FetchDistrictsDTO districtsDTO = departmentService.getDistrictsByDepartment(deptHead.getDepartment().getId(), pageNumber, pageSize, sortBy, sortOrder, keyword);
        return ResponseEntity.ok(districtsDTO);
    }

    @PostMapping("/create-district")
    public ResponseEntity<?> createDistrict(@RequestBody String name) {
        try {
            User loggedInUser = authUtil.getLoggedInUser();
            if (!loggedInUser.getRole().equals(User.Role.DEPT_HEAD)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to create a department.");
            }
            Department department = departmentService.findDepartmentByName(loggedInUser.getDepartment().getName());
            if(name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Department name cannot be empty.");
            }
            districtService.createDistrict(name, department);
            return ResponseEntity.status(HttpStatus.CREATED).body("Department created successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());

        }
    }

    @GetMapping("/view-district-head/{name}")
    public ResponseEntity<?> getDistrictHeadByName(@PathVariable String name) {
        try {
            User loggedInUser = authUtil.getLoggedInUser();
            if (!loggedInUser.getRole().equals(User.Role.DEPT_HEAD)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
            }
            FetchUserDTO userDTO = districtService.findDistrictHeadByDistrictName(loggedInUser.getDepartment(),name);
            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());

        }
    }
}