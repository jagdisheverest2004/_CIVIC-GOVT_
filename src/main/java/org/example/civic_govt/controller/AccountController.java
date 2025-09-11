package org.example.civic_govt.controller;

import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.accounts.CreateAccountDTO;
import org.example.civic_govt.payload.accounts.FetchAccountDTO;
import org.example.civic_govt.service.AccountService;
import org.example.civic_govt.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/account")
public class AccountController {

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private AccountService accountService;

    @PostMapping("/create-department-head")
    public ResponseEntity<?> createDepartmentAdminAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        User loggedInUser = authUtil.getLoggedInUser();
        try{
            if(loggedInUser.getRole().equals(User.Role.ADMIN)){
                FetchAccountDTO fetchAccountDTO = accountService.createDepartmentHeadAccount(createAccountDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(fetchAccountDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to create a department head account.");
            }
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/create-district-head")
    public ResponseEntity<?> createDistrictAdminAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        User loggedInUser = authUtil.getLoggedInUser();
        try{
            if(loggedInUser.getRole().equals(User.Role.DEPT_HEAD)){
                FetchAccountDTO fetchAccountDTO = accountService.createDistrictHeadAccount(createAccountDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(fetchAccountDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to create a department head account.");
            }
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/create-zone-head")
    public ResponseEntity<?> createZoneAdminAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        User loggedInUser = authUtil.getLoggedInUser();
        try{
            if(loggedInUser.getRole().equals(User.Role.DISTRICT_HEAD)){
                FetchAccountDTO fetchAccountDTO = accountService.createZoneHeadAccount(createAccountDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(fetchAccountDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to create a department head account.");
            }
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/create-zone-subordinate")
    public ResponseEntity<?> createZoneSubOrdinateAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        User loggedInUser = authUtil.getLoggedInUser();
        try{
            if(loggedInUser.getRole().equals(User.Role.ZONE_HEAD)){
                FetchAccountDTO fetchAccountDTO = accountService.createZoneSubOrdinateAccount(createAccountDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body(fetchAccountDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to create a department head account.");
            }
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
