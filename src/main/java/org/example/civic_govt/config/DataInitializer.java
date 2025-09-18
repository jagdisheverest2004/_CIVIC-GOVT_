//package org.example.civic_govt.config;
//
//import org.example.civic_govt.model.Department;
//import org.example.civic_govt.model.District;
//import org.example.civic_govt.model.User;
//import org.example.civic_govt.model.Zone;
//import org.example.civic_govt.repository.DepartmentRepository;
//import org.example.civic_govt.repository.DistrictRepository;
//import org.example.civic_govt.repository.UserRepository;
//import org.example.civic_govt.repository.ZoneRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class DataInitializer {
//
//    @Bean
//    public CommandLineRunner initDefaultUsersAndDepartments(
//            UserRepository userRepository,
//            DepartmentRepository departmentRepository,
//            DistrictRepository districtRepository,
//            ZoneRepository zoneRepository,
//            PasswordEncoder passwordEncoder) {
//        return args -> {
//            // --- 1. Create a global ADMIN user ---
//            User adminUser = userRepository.findByUsername("admin")
//                    .orElseGet(() -> {
//                        User newAdmin = new User("admin", "admin@civicgovt.com", passwordEncoder.encode("adminpass"), User.Role.ADMIN);
//                        return userRepository.save(newAdmin);
//                    });
//            System.out.println("Admin user created: " + adminUser.getUsername());
//
//            // --- 2. Create Departments, their hierarchy, and officials ---
//            List<String> agriIssues = Arrays.asList("Farm water issues", "Irrigation system failure", "Agriculture infrastructure damage", "Crop support queries");
//            createDepartmentAndHierarchy(
//                    departmentRepository, districtRepository, zoneRepository, userRepository, passwordEncoder,
//                    "Agriculture & Farmers Welfare", "head_agri", "dist_head_agri", "zone_head_agri", "sub_agri", agriIssues
//            );
//
//            List<String> energyIssues = Arrays.asList("Electricity", "Street lights", "Power outages", "Grid issues");
//            createDepartmentAndHierarchy(
//                    departmentRepository, districtRepository, zoneRepository, userRepository, passwordEncoder,
//                    "Energy", "head_energy", "dist_head_energy", "zone_head_energy", "sub_energy", energyIssues
//            );
//
//            List<String> municipalIssues = Arrays.asList("Local roads", "Water supply", "Garbage", "Drains", "Public lighting");
//            createDepartmentAndHierarchy(
//                    departmentRepository, districtRepository, zoneRepository, userRepository, passwordEncoder,
//                    "Municipal Administration & Water Supply", "head_municipal", "dist_head_municipal", "zone_head_municipal", "sub_municipal", municipalIssues
//            );
//
//            System.out.println("Default users, departments, districts, and zones initialized.");
//        };
//    }
//
//    private void createDepartmentAndHierarchy(
//            DepartmentRepository departmentRepository,
//            DistrictRepository districtRepository,
//            ZoneRepository zoneRepository,
//            UserRepository userRepository,
//            PasswordEncoder passwordEncoder,
//            String deptName, String deptHeadUsername, String distHeadUsername, String zoneHeadUsername, String subUsername, List<String> defaultIssues) {
//
//        // Create Department Head
//        User deptHead = userRepository.findByUsername(deptHeadUsername).orElseGet(() -> {
//            User newHead = new User(deptHeadUsername, deptHeadUsername + "@civicgovt.com", passwordEncoder.encode("password"), User.Role.DEPT_HEAD);
//            return userRepository.save(newHead);
//        });
//
//        // Create Department
//        Department department = departmentRepository.findByName(deptName).orElseGet(() -> {
//            Department newDepartment = new Department();
//            newDepartment.setName(deptName);
//            newDepartment.setDeptHead(deptHead);
//            newDepartment.setDefaultIssueTypes(defaultIssues);
//            return departmentRepository.save(newDepartment);
//        });
//
//        // Link Department Head to the Department
//        if (deptHead.getDepartment() == null) {
//            deptHead.setDepartment(department);
//            userRepository.save(deptHead);
//        }
//
//        // Create District Head
//        User distHead = userRepository.findByUsername(distHeadUsername).orElseGet(() -> {
//            User newHead = new User(distHeadUsername, distHeadUsername + "@civicgovt.com", passwordEncoder.encode("password"), User.Role.DISTRICT_HEAD);
//            return userRepository.save(newHead);
//        });
//
//        // Create District
//        District district = districtRepository.findByName("North " + deptName.replace(" ", ""))
//                .orElseGet(() -> {
//                    District newDistrict = new District();
//                    newDistrict.setName("North " + deptName.replace(" ", ""));
//                    newDistrict.setDepartment(department);
//                    newDistrict.setDistHead(distHead);
//                    return districtRepository.save(newDistrict);
//                });
//
//        // Link District Head to the Department and District
//        if (distHead.getDepartment() == null) {
//            distHead.setDepartment(department);
//        }
//        if (distHead.getDistrict() == null) {
//            distHead.setDistrict(district);
//        }
//        userRepository.save(distHead);
//
//        // Create Zone Head
//        User zoneHead = userRepository.findByUsername(zoneHeadUsername).orElseGet(() -> {
//            User newHead = new User(zoneHeadUsername, zoneHeadUsername + "@civicgovt.com", passwordEncoder.encode("password"), User.Role.ZONE_HEAD);
//            return userRepository.save(newHead);
//        });
//
//        // Create Zone
//        Zone zone = zoneRepository.findZoneByDistrictName(district, "Zone 1 " + deptName.replace(" ", ""));
//        if(zone==null) {
//            Zone newZone = new Zone();
//            newZone.setName("Zone 1 " + deptName.replace(" ", ""));
//            newZone.setDistrict(district);
//            newZone.setZoneHead(zoneHead);
//            zone = zoneRepository.save(newZone); // CORRECTED: Assign the newly saved zone to the 'zone' variable
//        }
//
//        // Link Zone Head to the hierarchy
//        if (zoneHead.getDepartment() == null) {
//            zoneHead.setDepartment(department);
//        }
//        if (zoneHead.getDistrict() == null) {
//            zoneHead.setDistrict(district);
//        }
//        if (zoneHead.getZone() == null) {
//            zoneHead.setZone(zone);
//        }
//        userRepository.save(zoneHead);
//
//        // Create Subordinate Official
//        User subordinate = userRepository.findByUsername(subUsername).orElseGet(() -> {
//            User newSub = new User(subUsername, subUsername + "@civicgovt.com", passwordEncoder.encode("password"), User.Role.SUBORDINATE);
//            return userRepository.save(newSub);
//        });
//
//        // Link Subordinate to the hierarchy
//        if (subordinate.getDepartment() == null) {
//            subordinate.setDepartment(department);
//        }
//        if (subordinate.getDistrict() == null) {
//            subordinate.setDistrict(district);
//        }
//        if (subordinate.getZone() == null) {
//            subordinate.setZone(zone);
//        }
//        userRepository.save(subordinate);
//
//        System.out.println("Department '" + deptName + "' and its hierarchy created.");
//    }
//}