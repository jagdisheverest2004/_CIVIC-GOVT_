package org.example.civic_govt.repository;

import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.issues.IssueFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> , JpaSpecificationExecutor<Department> {
    Optional<Department> findByName(String name);

    @Query("SELECT d.districtOfficials from Department d WHERE d.id = ?1")
    Page<User> findDistrictOfficialsByDepartmentId(Pageable pageable, Long id);

    @Query("SELECT d FROM Department d WHERE d.deptHead.id = ?1")
    Optional<Department> findDepartmentByDeptHeadId(Long id);
}
