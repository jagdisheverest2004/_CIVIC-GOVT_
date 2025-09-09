package org.example.civic_govt.repository;

import org.example.civic_govt.model.User;
import org.example.civic_govt.model.Vote;
import org.example.civic_govt.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT v FROM Vote v WHERE v.issue = ?1 AND v.user = ?2")
    Optional<Vote> findByIssueAndUser(Issue issue, User user);
}

