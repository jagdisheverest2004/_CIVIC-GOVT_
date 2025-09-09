package org.example.civic_govt.controller;

import org.example.civic_govt.model.Community;
import org.example.civic_govt.model.Department;
import org.example.civic_govt.service.CommunityService;
import org.example.civic_govt.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/admin")
public class AdministrationController {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/communities")
    public ResponseEntity<List<Community>> getAllCommunities() {
        return new ResponseEntity<>(communityService.findAllCommunities(), HttpStatus.OK);
    }

    @PostMapping("/communities")
    public ResponseEntity<Community> createCommunity(@RequestBody Community community) {
        Community newCommunity = communityService.createCommunity(community);
        return new ResponseEntity<>(newCommunity, HttpStatus.CREATED);
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return new ResponseEntity<>(departmentService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department newDepartment = departmentService.createDepartment(department);
        return new ResponseEntity<>(newDepartment, HttpStatus.CREATED);
    }
}