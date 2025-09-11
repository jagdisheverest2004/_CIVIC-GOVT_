package org.example.civic_govt.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.civic_govt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userName);

    boolean existsByUsername(@NotBlank @Size(min = 5, max = 20) String username);

    boolean existsByEmail(@NotBlank @Size(max = 50) @Email String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.id = ?1 AND u.zone.id = ?2")
    Boolean isUserInZone(Long assigneeId, Long id);

    @Query("SELECT u FROM User u WHERE u.department.id = ?1 AND u.role IN ('DEPT_HEAD', 'DISTRICT_HEAD', 'ZONE_HEAD', 'SUBORDINATE')")
    List<User> findOfficialsByDepartmentId(Long departmentId);
}