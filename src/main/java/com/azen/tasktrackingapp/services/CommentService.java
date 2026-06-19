package com.azen.tasktrackingapp.services;

import com.azen.tasktrackingapp.models.Comment;

import java.util.List;

public interface CommentService
{
    Comment createComment(Long taskId, Comment comment, Long authorId);
    List<Comment> getCommentsByTask(Long taskId);
    void deleteComment(Long id, Long requesterId);
}