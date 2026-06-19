package com.azen.tasktrackingapp.services;

import com.azen.tasktrackingapp.models.Task;
import com.azen.tasktrackingapp.models.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TaskService
{
    Task createTask(Long projectId, Task task);
    Task getTaskById(Long id);
    Page<Task> getTasksByProject(Long projectId, Status status, Pageable pageable);
    Task updateTask(Long id, Task task);
    void deleteTask(Long id);
    List<Task> getTasksDueToday();
    List<Task> getTasksByAssignee(Long userId);
}

