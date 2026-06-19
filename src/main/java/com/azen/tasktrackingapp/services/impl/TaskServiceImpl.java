package com.azen.tasktrackingapp.services.impl;

import com.azen.tasktrackingapp.exceptions.ResourceNotFoundException;
import com.azen.tasktrackingapp.models.Task;
import com.azen.tasktrackingapp.models.enums.Status;
import com.azen.tasktrackingapp.repositories.ProjectRepository;
import com.azen.tasktrackingapp.repositories.TaskRepository;
import com.azen.tasktrackingapp.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService
{

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Override
    public Task createTask(Long projectId, Task task)
    {
        return projectRepository.findById(projectId)
                .map(project -> {
                    task.setProject(project);
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
    }

    @Override
    public Task getTaskById(Long id)
    {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    @Override
    public Page<Task> getTasksByProject(Long projectId, Status status, Pageable pageable) {
        if (status != null)
        {
            return taskRepository.findByProjectIdAndStatus(projectId, status, pageable);
        }
        return taskRepository.findByProjectId(projectId, pageable);
    }

    @Override
    public Task updateTask(Long id, Task task)
    {
        Task existing = getTaskById(id);
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setStatus(task.getStatus());
        existing.setPriority(task.getPriority());
        existing.setDueDate(task.getDueDate());
        return taskRepository.save(existing);
    }

    @Override
    public void deleteTask(Long id)
    {
        Task existing = getTaskById(id);
        taskRepository.delete(existing);
    }

    @Override
    public List<Task> getTasksDueToday()
    {
        return taskRepository.findTasksDueOn(LocalDate.now());
    }

    @Override
    public List<Task> getTasksByAssignee(Long userId)
    {
        return taskRepository.findByAssigneeId(userId);
    }
}
