package org.example.civic_govt.controller;

import org.example.civic_govt.dto.CommentRequest;
import org.example.civic_govt.model.Comment;
import org.example.civic_govt.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/issues/{issueId}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long issueId, @RequestParam Long userId, @Valid @RequestBody CommentRequest request) {
        Comment comment = commentService.addComment(issueId, userId, request.getText());
        return ResponseEntity.ok(comment);
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long issueId) {
        return ResponseEntity.ok(commentService.getComments(issueId));
    }
}

