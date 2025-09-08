package org.example.civic_govt.repository;

import org.example.civic_govt.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByCategory(String category);
    List<Issue> findByStatus(Issue.Status status);
}

