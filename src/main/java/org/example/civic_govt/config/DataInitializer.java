package org.example.civic_govt.config;

import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Department;
import org.example.civic_govt.repository.UserRepository;
import org.example.civic_govt.repository.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDefaultUsersAndDepartments(
            UserRepository userRepository,
            DepartmentRepository departmentRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {

            // Pre-create some users for roles
            User adminUser = userRepository.findByUsername("admin")
                    .orElseGet(() -> {
                        User newAdmin = new User();
                        newAdmin.setUsername("admin");
                        newAdmin.setEmail("admin1@civicgovt.com");
                        newAdmin.setPassword(passwordEncoder.encode("adminpass"));
                        newAdmin.setRole(User.Role.ADMIN);
                        return userRepository.save(newAdmin);
                    });

            User headUser1 = userRepository.findByUsername("head_user1")
                    .orElseGet(() -> {
                        User newHead = new User();
                        newHead.setUsername("head_user1");
                        newHead.setEmail("head1@civicgovt.com");
                        newHead.setPassword(passwordEncoder.encode("headpass1"));
                        newHead.setRole(User.Role.OFFICIAL); // Change to official role
                        return userRepository.save(newHead);
                    });

            User officialUser1 = userRepository.findByUsername("official_user1")
                    .orElseGet(() -> {
                        User newOfficial = new User();
                        newOfficial.setUsername("official_user1");
                        newOfficial.setEmail("official1@civicgovt.com");
                        newOfficial.setPassword(passwordEncoder.encode("officialpass1"));
                        newOfficial.setRole(User.Role.OFFICIAL);
                        return userRepository.save(newOfficial);
                    });

            // Initialize all departments with default issues
            List<String> agriIssues = Arrays.asList("Farm water issues", "Irrigation system failure", "Agriculture infrastructure damage", "Crop support queries");
            createAndSaveDepartment(departmentRepository, "Agriculture & Farmers Welfare", headUser1, agriIssues);

            List<String> energyIssues = Arrays.asList("Power outages", "Street light not working", "Damaged power lines");
            createAndSaveDepartment(departmentRepository, "Energy", officialUser1, energyIssues);

            List<String> environmentIssues = Arrays.asList("Illegal tree cutting", "Industrial pollution", "Forest encroachment");
            createAndSaveDepartment(departmentRepository, "Environment & Forests (Climate Change)", null, environmentIssues);

            List<String> municipalIssues = Arrays.asList("Potholes", "Garbage collection issues", "Clogged drains", "Public lighting failure");
            createAndSaveDepartment(departmentRepository, "Municipal Administration & Water Supply", null, municipalIssues);

            List<String> transportIssues = Arrays.asList("Public transport delays", "Damaged roads", "Traffic signal malfunction");
            createAndSaveDepartment(departmentRepository, "Transport", null, transportIssues);

            System.out.println("Default users and departments initialized.");
        };
    }

    private Department createAndSaveDepartment(DepartmentRepository departmentRepository, String name, User head, List<String> issues) {
        return departmentRepository.findByName(name)
                .orElseGet(() -> {
                    Department newDepartment = new Department();
                    newDepartment.setName(name);
                    newDepartment.setHead(head);
                    newDepartment.setDefaultIssueTypes(issues);
                    return departmentRepository.save(newDepartment);
                });
    }
}