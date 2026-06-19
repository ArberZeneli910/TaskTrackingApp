package com.azen.tasktrackingapp.controllers;

import com.azen.tasktrackingapp.models.Project;
import com.azen.tasktrackingapp.models.Task;
import com.azen.tasktrackingapp.models.enums.Status;
import com.azen.tasktrackingapp.services.ProjectService;
import com.azen.tasktrackingapp.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.azen.tasktrackingapp.security.CustomUserDetails;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController
{
    private final ProjectService projectService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody @Valid Project project)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(project));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id)
    {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping
    public ResponseEntity<Page<Project>> getAllProjects(
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        return ResponseEntity.ok(projectService.getProjectsByOwner(userDetails.getId(), pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody @Valid Project project)
    {
        return ResponseEntity.ok(projectService.updateProject(id, project));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id)
    {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<Task> createTask(@PathVariable Long projectId, @RequestBody @Valid Task task)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(projectId, task));
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<?> getTasksByProject(@PathVariable Long projectId, @RequestParam(required = false) Status status, Pageable pageable)
    {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId, status, pageable));
    }
}