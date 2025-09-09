package org.example.civic_govt.repository;

import org.example.civic_govt.model.Comment;
import org.example.civic_govt.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

