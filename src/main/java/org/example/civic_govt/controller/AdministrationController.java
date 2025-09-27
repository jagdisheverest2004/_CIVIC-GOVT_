package org.example.civic_govt.controller;

import org.example.civic_govt.config.AppConstants;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.departments.CreateDepartmentDTO;
import org.example.civic_govt.payload.departments.FetchDepartmentDTO;
import org.example.civic_govt.payload.departments.FetchDepartmentsDTO;
import org.example.civic_govt.payload.districts.FetchDistrictsDTO;
import org.example.civic_govt.payload.users.FetchUserDTO;
import org.example.civic_govt.service.DepartmentService;
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
        }
        catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());

        }
    }

    @GetMapping("/departments")
    public ResponseEntity<?> getDepartments(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_DISTRICTS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DISTRICTS_DIR) String sortOrder,
            @RequestParam(name = "keywords", required = false) String keyword
    ) {
        User admin = authUtil.getLoggedInUser();
        if (!admin.getRole().equals(User.Role.ADMIN)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }

        FetchDepartmentsDTO departmentsDTO = departmentService.getAllDepartments(pageNumber, pageSize, sortBy, sortOrder, keyword);
        return ResponseEntity.ok(departmentsDTO);
    }

    @GetMapping("/view-department-head/{name}")
    public ResponseEntity<?> getDepartmentHeadByName(@PathVariable String name) {
        try {
            User loggedInUser = authUtil.getLoggedInUser();
            if (!loggedInUser.getRole().equals(User.Role.ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
            }
            FetchUserDTO userDTO = departmentService.findDepartmentHeadByDepartmentName(name);
            return ResponseEntity.ok(userDTO);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("view-department/{name}")
    public ResponseEntity<?> getDepartmentByName(@PathVariable String name) {
        try {
            User loggedInUser = authUtil.getLoggedInUser();
            if (!loggedInUser.getRole().equals(User.Role.ADMIN)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
            }
            FetchDepartmentDTO departmentDTO = departmentService.getDepartmentByName(name);
            return ResponseEntity.ok(departmentDTO);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/districts")
    public ResponseEntity<?> getAllDistricts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_DISTRICTS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DISTRICTS_DIR) String sortOrder,
            @RequestParam(name = "keywords", required = false) String keyword,
            @PathVariable(name = "id") Long departmentId
    ) {
        User admin = authUtil.getLoggedInUser();
        if (!admin.getRole().equals(User.Role.ADMIN)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to view this resource.");
        }

        FetchDistrictsDTO districtsDTO = departmentService.getDistrictsByDepartment(departmentId, pageNumber, pageSize, sortBy, sortOrder, keyword);
        return ResponseEntity.ok(districtsDTO);
    }
}