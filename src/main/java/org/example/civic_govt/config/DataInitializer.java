package org.example.civic_govt.config;

import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Department;
import org.example.civic_govt.repository.UserRepository;
import org.example.civic_govt.repository.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDefaultUsersAndDepartments(
            UserRepository userRepository,
            DepartmentRepository departmentRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Check and create a default Admin user if they don't exist
            User adminUser1 = userRepository.findByUsername("admin_user1")
                    .orElseGet(() -> {
                        User newAdmin = new User();
                        newAdmin.setUsername("admin_user1");
                        newAdmin.setEmail("admin1@civicgovt.com");
                        newAdmin.setPassword(passwordEncoder.encode("adminpass1"));
                        newAdmin.setRole(User.Role.ADMIN);
                        return userRepository.save(newAdmin);
                    });

            // Check and create a default Official user if they don't exist
            User officialUser1 = userRepository.findByUsername("official_user1")
                    .orElseGet(() -> {
                        User newOfficial = new User();
                        newOfficial.setUsername("official_user1");
                        newOfficial.setEmail("official1@civicgovt.com");
                        newOfficial.setPassword(passwordEncoder.encode("officialpass1"));
                        newOfficial.setRole(User.Role.OFFICIAL);
                        return userRepository.save(newOfficial);
                    });

            User adminUser2 = userRepository.findByUsername("admin_user2")
                    .orElseGet(() -> {
                        User newAdmin = new User();
                        newAdmin.setUsername("admin_user2");
                        newAdmin.setEmail("admin2@civicgovt.com");
                        newAdmin.setPassword(passwordEncoder.encode("adminpass2"));
                        newAdmin.setRole(User.Role.ADMIN);
                        return userRepository.save(newAdmin);
                    });

            // Check and create a default Official user if they don't exist
            User officialUser2 = userRepository.findByUsername("official_user2")
                    .orElseGet(() -> {
                        User newOfficial = new User();
                        newOfficial.setUsername("official_user2");
                        newOfficial.setEmail("official2@civicgovt.com");
                        newOfficial.setPassword(passwordEncoder.encode("officialpass2"));
                        newOfficial.setRole(User.Role.OFFICIAL);
                        return userRepository.save(newOfficial);
                    });

            // Check and create a default department if it doesn't exist
            Department publicWorks = departmentRepository.findByName("Public Works")
                    .orElseGet(() -> {
                        Department newDepartment = new Department();
                        newDepartment.setName("Public Works");
                        return departmentRepository.save(newDepartment);
                    });

            Department transportWorks = departmentRepository.findByName("Transport Works")
                    .orElseGet(() -> {
                        Department newDepartment = new Department();
                        newDepartment.setName("Transport Works");
                        return departmentRepository.save(newDepartment);
                    });

            // Assign Admin as the head of the department
            publicWorks.setHead(adminUser1);
            departmentRepository.save(publicWorks);

            // Assign Official to the department
            officialUser1.setDepartment(publicWorks);
            userRepository.save(officialUser1);

            // Assign Admin as the head of the department
            transportWorks.setHead(adminUser2);
            departmentRepository.save(transportWorks);

            // Assign Official to the department
            officialUser2.setDepartment(transportWorks);
            userRepository.save(officialUser2);

            System.out.println("Default users and departments initialized.");
        };
    }
}