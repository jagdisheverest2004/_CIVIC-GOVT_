package org.example.civic_govt.repository;

import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface DistrictRepository extends JpaRepository<District, Long> {

    @Query("SELECT d FROM District d WHERE d.department = ?1 AND d.name = ?2")
    District findDistrictByDepartmentName(Department department, String name);

    Optional<District> findByName(String name);
}
