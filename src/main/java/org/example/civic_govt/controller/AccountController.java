package org.example.civic_govt.controller;

import org.example.civic_govt.payload.accounts.CreateAccountDTO;
import org.example.civic_govt.payload.accounts.FetchAccountDTO;
import org.example.civic_govt.service.AccountService;
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
    private AccountService accountService;

    @PostMapping("/create-department-head")
    public ResponseEntity<?> createDepartmentAdminAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        try{
            FetchAccountDTO fetchAccountDTO = accountService.createDepartmentHeadAccount(createAccountDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(fetchAccountDTO);
        }
        catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/create-district-head")
    public ResponseEntity<?> createDistrictAdminAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        try{
            FetchAccountDTO fetchAccountDTO = accountService.createDistrictHeadAccount(createAccountDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(fetchAccountDTO);
        }
        catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/create-zone-head")
    public ResponseEntity<?> createZoneAdminAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        try{
            FetchAccountDTO fetchAccountDTO = accountService.createZoneHeadAccount(createAccountDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(fetchAccountDTO);
        }
        catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/create-zone-subordinate")
    public ResponseEntity<?> createZoneSubOrdinateAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        try{
            FetchAccountDTO fetchAccountDTO = accountService.createZoneSubOrdinateAccount(createAccountDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(fetchAccountDTO);
        }
        catch (SecurityException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
