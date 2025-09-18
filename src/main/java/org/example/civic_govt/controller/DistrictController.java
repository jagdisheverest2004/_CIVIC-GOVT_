package org.example.civic_govt.controller;

import org.example.civic_govt.config.AppConstants;
import org.example.civic_govt.model.District;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.issues.FetchIssuesDTO;
import org.example.civic_govt.payload.issues.IssueFilterDTO;
import org.example.civic_govt.payload.users.FetchUsersDTO;
import org.example.civic_govt.payload.zones.FetchZonesDTO;
import org.example.civic_govt.service.DistrictService;
import org.example.civic_govt.service.IssueService;
import org.example.civic_govt.service.ZoneService;
import org.example.civic_govt.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/districts")
public class DistrictController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private DistrictService districtService;

    @Autowired
    private ZoneService zoneService;

    @GetMapping("/issues")
    public ResponseEntity<?> getDistrictIssues(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ISSUES_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ISSUES_DIR) String sortOrder,
            @RequestBody(required = false) IssueFilterDTO filters) {

        User distHead = authUtil.getLoggedInUser();
        if (!distHead.getRole().equals(User.Role.DISTRICT_HEAD) || distHead.getDistrict() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }

        if (filters == null) {
            filters = new IssueFilterDTO();
        }

        FetchIssuesDTO issuesDTO = issueService.getIssuesWithFilters(pageNumber, pageSize, sortBy, sortOrder, filters);
        return ResponseEntity.ok(issuesDTO);
    }

    @GetMapping("/officials")
    public ResponseEntity<?> getDistrictOfficials(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_USERS_DIR) String sortOrder
    ) {
        User distHead = authUtil.getLoggedInUser();
        if (!distHead.getRole().equals(User.Role.DISTRICT_HEAD) || distHead.getDistrict() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }
        FetchUsersDTO usersDTO = districtService.getZoneHeadsByDistrict(distHead.getDistrict().getId(), pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(usersDTO);
    }

    @PostMapping("/create-zone")
    public ResponseEntity<?> createZone(@RequestBody String name) {
        try {
            User loggedInUser = authUtil.getLoggedInUser();
            if (!loggedInUser.getRole().equals(User.Role.DISTRICT_HEAD)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to create a department.");
            }
            if(name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Department name cannot be empty.");
            }
            District district = districtService.findDistrictByName(name);
            zoneService.createZone(name, district);
            return ResponseEntity.status(HttpStatus.CREATED).body("Department created successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/zones")
    public ResponseEntity<?> getDistrictZones(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_USERS_DIR) String sortOrder,
            @RequestParam(name = "keyword", required = false) String keyword
    ) {
        User distHead = authUtil.getLoggedInUser();
        if (!distHead.getRole().equals(User.Role.DISTRICT_HEAD) || distHead.getDistrict() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }
        FetchZonesDTO zonesDTO = districtService.getZonesByDistrict(distHead.getDistrict().getId(), pageNumber, pageSize, sortBy, sortOrder, keyword);
        return ResponseEntity.ok(zonesDTO);
    }


}