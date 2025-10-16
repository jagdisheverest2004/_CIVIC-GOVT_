package org.example.civic_govt.controller;

import org.example.civic_govt.config.AppConstants;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.issues.FetchIssuesDTO;
import org.example.civic_govt.payload.issues.IssueFilterDTO;
import org.example.civic_govt.payload.users.FetchUsersDTO;
import org.example.civic_govt.payload.zones.FetchZoneDTO;
import org.example.civic_govt.service.IssueService;
import org.example.civic_govt.service.UserService;
import org.example.civic_govt.service.ZoneService;
import org.example.civic_govt.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/zones")
public class ZoneController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ZoneService zoneService;

    @GetMapping("/issues")
    public ResponseEntity<?> getZoneIssues(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ISSUES_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ISSUES_DIR) String sortOrder,
            @RequestBody(required = false) IssueFilterDTO filters) {

        User zoneHead = authUtil.getLoggedInUser();
        if (!zoneHead.getRole().equals(User.Role.ZONE_HEAD) || zoneHead.getZone() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }

        if (filters == null) {
            filters = new IssueFilterDTO();
        }

        FetchIssuesDTO issuesDTO = issueService.getIssuesWithFilters(pageNumber, pageSize, sortBy, sortOrder, filters);
        return ResponseEntity.ok(issuesDTO);
    }

    @GetMapping("/officials")
    public ResponseEntity<?> getZoneOfficials(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_USERS_DIR) String sortOrder
    ) {
        User zoneHead = authUtil.getLoggedInUser();
        if (!zoneHead.getRole().equals(User.Role.ZONE_HEAD) || zoneHead.getZone() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }
        FetchUsersDTO usersDTO = zoneService.getOfficialsInZone(zoneHead.getZone(), pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(usersDTO);
    }

    @GetMapping("/view-zone")
    public ResponseEntity<?> getZoneDetails() {
        User zoneHead = authUtil.getLoggedInUser();
        if (!zoneHead.getRole().equals(User.Role.ZONE_HEAD) || zoneHead.getZone() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }
        FetchZoneDTO zoneDTO = zoneService.getZoneDetails(zoneHead.getZone().getId());
        return ResponseEntity.ok(zoneDTO);
    }
}