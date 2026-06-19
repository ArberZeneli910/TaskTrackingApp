package com.azen.tasktrackingapp.services.impl;

import com.azen.tasktrackingapp.exceptions.ResourceNotFoundException;
import com.azen.tasktrackingapp.models.Comment;
import com.azen.tasktrackingapp.models.Task;
import com.azen.tasktrackingapp.models.User;
import com.azen.tasktrackingapp.repositories.CommentRepository;
import com.azen.tasktrackingapp.repositories.TaskRepository;
import com.azen.tasktrackingapp.repositories.UserRepository;
import com.azen.tasktrackingapp.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService
{

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Comment createComment(Long taskId, Comment comment, Long authorId)
    {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + authorId));

        comment.setTask(task);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByTask(Long taskId)
    {
        return commentRepository.findByTaskId(taskId);
    }

    @Override
    public void deleteComment(Long id, Long requesterId)
    {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        if (!comment.getAuthor().getId().equals(requesterId))
        {
            throw new AccessDeniedException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }
}