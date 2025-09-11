package org.example.civic_govt.service;

import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.District;
import org.example.civic_govt.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistrictService {

    @Autowired
    private DistrictRepository districtRepository;

    public void createDistrict(String name, Department department) {
        if(districtRepository.findDistrictByDepartmentName(department, name) != null) {
            throw new IllegalArgumentException("District with name " + name + " already exists in department " + department.getName());
        }
        District district = new District();
        district.setName(name);
        district.setDepartment(department);
        districtRepository.save(district);
    }

    public District findDistrictByName(String name) {
        return districtRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("District not found with name " + name));
    }
}
