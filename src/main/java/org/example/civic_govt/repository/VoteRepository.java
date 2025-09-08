package org.example.civic_govt.repository;

import org.example.civic_govt.model.Vote;
import org.example.civic_govt.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByIssue(Issue issue);
    long countByIssue(Issue issue);
}

