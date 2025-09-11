package org.example.civic_govt.controller;

import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.District;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.departments.CreateDepartmentDTO;
import org.example.civic_govt.payload.districts.CreateDistrictDTO;
import org.example.civic_govt.service.DepartmentService;
import org.example.civic_govt.service.DistrictService;
import org.example.civic_govt.service.ZoneService;
import org.example.civic_govt.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/admin")
public class AdministrationController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/create-department")
    public ResponseEntity<?> createDepartment(@RequestBody CreateDepartmentDTO createDepartmentDTO) {
        try {
            User loggedInUser = authUtil.getLoggedInUser();
            if (!loggedInUser.getRole().equals(User.Role.ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to create a department.");
            }
            String name = createDepartmentDTO.getName();
            if(name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Department name cannot be empty.");
            }
            departmentService.createDepartment(name);
            return ResponseEntity.status(HttpStatus.CREATED).body("Department created successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());

        }
    }

    @PostMapping("/create-district")
    public ResponseEntity<?> createDistrict(@RequestBody CreateDistrictDTO createDistrictDTO) {
        try {
            User loggedInUser = authUtil.getLoggedInUser();
            if (!loggedInUser.getRole().equals(User.Role.ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to create a department.");
            }
            String name = createDistrictDTO.getName();
            if(name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Department name cannot be empty.");
            }
            Department department = departmentService.findDepartmentByName(createDistrictDTO.getDepartmentName());
            districtService.createDistrict(name, department);
            return ResponseEntity.status(HttpStatus.CREATED).body("Department created successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());

        }
    }

    @PostMapping("/create-zone")
    public ResponseEntity<?> createZone(@RequestBody String name) {
        try {
            User loggedInUser = authUtil.getLoggedInUser();
            if (!loggedInUser.getRole().equals(User.Role.ADMIN)) {
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

}