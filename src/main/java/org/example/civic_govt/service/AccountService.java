package org.example.civic_govt.service;

import jakarta.transaction.Transactional;
import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.District;
import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Zone;
import org.example.civic_govt.payload.accounts.CreateAccountDTO;
import org.example.civic_govt.payload.accounts.FetchAccountDTO;
import org.example.civic_govt.repository.DepartmentRepository;
import org.example.civic_govt.repository.DistrictRepository;
import org.example.civic_govt.repository.UserRepository;
import org.example.civic_govt.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public FetchAccountDTO createDepartmentHeadAccount(CreateAccountDTO createAccountDTO) {
        if(userRepository.existsByUsername(createAccountDTO.getUsername())){
            throw new RuntimeException("Username is already taken!");
        }
        if(userRepository.existsByEmail(createAccountDTO.getEmail())){
            throw new RuntimeException("Email is already in use!");
        }
        String encodedPassword = passwordEncoder.encode(createAccountDTO.getPassword());
        User newUser = new User(createAccountDTO.getUsername(), createAccountDTO.getEmail(), encodedPassword);
        Department department = departmentRepository.findByName(createAccountDTO.getDepartmentName()).orElseThrow(() -> new RuntimeException("Department not found with name " + createAccountDTO.getDepartmentName()));
        if(department==null){
            throw new RuntimeException("Department not found with name " + createAccountDTO.getDepartmentName());
        }
        if(department.getDeptHead()!=null){
            throw new RuntimeException("Department " + createAccountDTO.getDepartmentName() + " already has a head.");
        }
        newUser.setDepartment(department);
        newUser.setRole(User.Role.DEPT_HEAD);
        userRepository.save(newUser);
        department.setDeptHead(newUser);
        departmentRepository.save(department);
        FetchAccountDTO fetchAccountDTO = new FetchAccountDTO(newUser.getId(), newUser.getUsername(), newUser.getEmail(), newUser.getRole().name());
        return fetchAccountDTO;
    }

    @Transactional
    public FetchAccountDTO createDistrictHeadAccount(CreateAccountDTO createAccountDTO) {
        if(userRepository.existsByUsername(createAccountDTO.getUsername())){
            throw new RuntimeException("Username is already taken!");
        }
        if(userRepository.existsByEmail(createAccountDTO.getEmail())){
            throw new RuntimeException("Email is already in use!");
        }
        String encodedPassword = passwordEncoder.encode(createAccountDTO.getPassword());
        User newUser = new User(createAccountDTO.getUsername(), createAccountDTO.getEmail(), encodedPassword);
        Department department = departmentRepository.findByName(createAccountDTO.getDepartmentName()).orElseThrow(() -> new RuntimeException("Department not found with name " + createAccountDTO.getDepartmentName()));
        if(department==null){
            throw new RuntimeException("Department not found with name " + createAccountDTO.getDepartmentName());
        }
        District district = districtRepository.findDistrictByDepartmentName(department,createAccountDTO.getDistrictName());
        if(district == null){
            throw new RuntimeException("District not found with name " + createAccountDTO.getDistrictName() + " in department " + createAccountDTO.getDepartmentName());
        }
        if(district.getDistHead()!=null){
            throw new RuntimeException("District " + createAccountDTO.getDistrictName() + " already has a head.");
        }
        newUser.setDepartment(department);
        newUser.setDistrict(district);
        newUser.setRole(User.Role.DISTRICT_HEAD);
        userRepository.save(newUser);
        district.setDistHead(newUser);
        districtRepository.save(district);
        department.getDistrictOfficials().add(newUser);
        departmentRepository.save(department);
        FetchAccountDTO fetchAccountDTO = new FetchAccountDTO(newUser.getId(), newUser.getUsername(), newUser.getEmail(), newUser.getRole().name());
        return fetchAccountDTO;
    }

    @Transactional
    public FetchAccountDTO createZoneHeadAccount(CreateAccountDTO createAccountDTO) {
        if(userRepository.existsByUsername(createAccountDTO.getUsername())){
            throw new RuntimeException("Username is already taken!");
        }
        if(userRepository.existsByEmail(createAccountDTO.getEmail())){
            throw new RuntimeException("Email is already in use!");
        }
        String encodedPassword = passwordEncoder.encode(createAccountDTO.getPassword());
        User newUser = new User(createAccountDTO.getUsername(), createAccountDTO.getEmail(), encodedPassword);
        Department department = departmentRepository.findByName(createAccountDTO.getDepartmentName()).orElseThrow(() -> new RuntimeException("Department not found with name " + createAccountDTO.getDepartmentName()));
        if(department==null){
            throw new RuntimeException("Department not found with name " + createAccountDTO.getDepartmentName());
        }
        District district = districtRepository.findDistrictByDepartmentName(department,createAccountDTO.getDistrictName());
        if(district == null){
            throw new RuntimeException("District not found with name " + createAccountDTO.getDistrictName() + " in department " + createAccountDTO.getDepartmentName());
        }
        Zone zone = zoneRepository.findZoneByDistrictName(district, createAccountDTO.getZoneName());
        if(zone == null){
            throw new RuntimeException("Zone not found with name " + createAccountDTO.getZoneName() + " in district " + createAccountDTO.getDistrictName());
        }
        if(zone.getZoneHead()!=null){
            throw new RuntimeException("Zone " + createAccountDTO.getZoneName() + " already has a head.");
        }
        newUser.setZone(zone);
        newUser.setDepartment(department);
        newUser.setDistrict(district);
        newUser.setRole(User.Role.ZONE_HEAD);
        userRepository.save(newUser);
        zone.setZoneHead(newUser);
        zoneRepository.save(zone);
        district.getZoneOfficials().add(newUser);
        districtRepository.save(district);
        FetchAccountDTO fetchAccountDTO = new FetchAccountDTO(newUser.getId(), newUser.getUsername(), newUser.getEmail(), newUser.getRole().name());
        return fetchAccountDTO;
    }

    @Transactional
    public FetchAccountDTO createZoneSubOrdinateAccount(CreateAccountDTO createAccountDTO) {
        if(userRepository.existsByUsername(createAccountDTO.getUsername())){
            throw new RuntimeException("Username is already taken!");
        }
        if(userRepository.existsByEmail(createAccountDTO.getEmail())){
            throw new RuntimeException("Email is already in use!");
        }
        String encodedPassword = passwordEncoder.encode(createAccountDTO.getPassword());
        User newUser = new User(createAccountDTO.getUsername(), createAccountDTO.getEmail(), encodedPassword);
        Department department = departmentRepository.findByName(createAccountDTO.getDepartmentName()).orElseThrow(() -> new RuntimeException("Department not found with name " + createAccountDTO.getDepartmentName()));
        if(department==null){
            throw new RuntimeException("Department not found with name " + createAccountDTO.getDepartmentName());
        }
        District district = districtRepository.findDistrictByDepartmentName(department,createAccountDTO.getDistrictName());
        if(district == null){
            throw new RuntimeException("District not found with name " + createAccountDTO.getDistrictName() + " in department " + createAccountDTO.getDepartmentName());
        }
        Zone zone = zoneRepository.findZoneByDistrictName(district, createAccountDTO.getZoneName());
        if(zone == null){
            throw new RuntimeException("Zone not found with name " + createAccountDTO.getZoneName() + " in district " + createAccountDTO.getDistrictName());
        }
        newUser.setZone(zone);
        newUser.setDepartment(department);
        newUser.setDistrict(district);
        newUser.setRole(User.Role.SUBORDINATE);
        userRepository.save(newUser);
        zone.getSubordinateOfficials().add(newUser);
        zoneRepository.save(zone);
        FetchAccountDTO fetchAccountDTO = new FetchAccountDTO(newUser.getId(), newUser.getUsername(), newUser.getEmail(), newUser.getRole().name());
        return fetchAccountDTO;
    }
}
