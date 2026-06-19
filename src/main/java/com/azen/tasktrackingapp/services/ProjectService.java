package com.azen.tasktrackingapp.services;

import com.azen.tasktrackingapp.models.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService
{
    Project createProject(Project project);
    Project getProjectById(Long id);
    Page<Project> getAllProjects(Pageable pageable);
    Project updateProject(Long id, Project project);
    void deleteProject(Long id);
    Page<Project> getProjectsByOwner(Long ownerId, Pageable pageable);
}
