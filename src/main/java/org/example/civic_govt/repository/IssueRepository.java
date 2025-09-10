package org.example.civic_govt.repository;

import org.example.civic_govt.model.Department;
import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByStatus(Issue.Status status);

    List<Issue> findByReporter(User reporter);

    List<Issue> findByDepartment(Department department);

    @Query("SELECT i.department FROM Issue i WHERE i.id = ?1")
    Department findDepartmentById(Long issueId);
}

