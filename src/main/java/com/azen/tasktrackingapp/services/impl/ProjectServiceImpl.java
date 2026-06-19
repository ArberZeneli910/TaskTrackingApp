package com.azen.tasktrackingapp.services.impl;

import com.azen.tasktrackingapp.exceptions.ResourceNotFoundException;
import com.azen.tasktrackingapp.models.Project;
import com.azen.tasktrackingapp.repositories.ProjectRepository;
import com.azen.tasktrackingapp.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService
{
    private final ProjectRepository projectRepository;

    @Override
    public Project createProject(Project project)
    {
        return projectRepository.save(project);
    }

    @Override
    public Project getProjectById(Long id)
    {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    @Override
    public Page<Project> getAllProjects(Pageable pageable)
    {
        return projectRepository.findAll(pageable);
    }

    @Override
    public Project updateProject(Long id, Project project)
    {
        Project existing = getProjectById(id);
        existing.setName(project.getName());
        existing.setDescription(project.getDescription());
        return projectRepository.save(existing);
    }

    @Override
    public void deleteProject(Long id)
    {
        Project existing = getProjectById(id);
        projectRepository.delete(existing);
    }

    @Override
    public Page<Project> getProjectsByOwner(Long ownerId, Pageable pageable)
    {
        return projectRepository.findByOwnerId(ownerId, pageable);
    }
}
