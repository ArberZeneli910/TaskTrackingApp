package com.azen.tasktrackingapp.controllers;

import com.azen.tasktrackingapp.models.User;
import com.azen.tasktrackingapp.services.TaskService;
import com.azen.tasktrackingapp.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id)
    {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable)
    {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<?> getTasksByUser(@PathVariable Long userId)
    {
        return ResponseEntity.ok(taskService.getTasksByAssignee(userId));
    }

}
