package com.azen.tasktrackingapp.controllers;

import com.azen.tasktrackingapp.models.Comment;
import com.azen.tasktrackingapp.security.CustomUserDetails;
import com.azen.tasktrackingapp.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController
{

    private final CommentService commentService;

    @PostMapping("/api/tasks/{taskId}/comments")
    public ResponseEntity<Comment> createComment(
            @PathVariable Long taskId,
            @RequestBody @Valid Comment comment,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(taskId, comment, userDetails.getId()));
    }

    @GetMapping("/api/tasks/{taskId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByTask(@PathVariable Long taskId)
    {
        return ResponseEntity.ok(commentService.getCommentsByTask(taskId));
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        commentService.deleteComment(id, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}