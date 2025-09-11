package org.example.civic_govt.service;

import org.example.civic_govt.model.Department;
import org.example.civic_govt.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public void createDepartment(String name) {
        if(departmentRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Department with name " + name + " already exists.");
        }
        Department department = new Department();
        department.setName(name);
        departmentRepository.save(department);
    }

    public Department findDepartmentByName(String name) {
        return departmentRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Department not found with name " + name));
    }


}