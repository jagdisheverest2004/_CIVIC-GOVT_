package org.example.civic_govt.repository;

import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.District;
import org.example.civic_govt.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;


public interface DistrictRepository extends JpaRepository<District, Long> , JpaSpecificationExecutor<District> {

    @Query("SELECT d FROM District d WHERE d.department = ?1 AND d.name = ?2")
    District findDistrictByDepartmentName(Department department, String name);

    Optional<District> findByName(String name);

    @Query("SELECT d.zoneOfficials from District d WHERE d.id = ?1")
    Page<User> findZoneHeadsByDistrictId(Long id, Pageable pageable);

    @Query("SELECT d FROM District d WHERE d.department.id = ?1")
    Page<District> findDistrictsByDepartmentId(Long id,Pageable pageable, Specification<District> spec);

    @Query("SELECT d FROM District d WHERE d.distHead.id = ?1")
    Optional<District> findDistrictByDistHeadId(Long id);
}
