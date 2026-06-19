package com.azen.tasktrackingapp.repositories;

import com.azen.tasktrackingapp.models.Task;
import com.azen.tasktrackingapp.models.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskRepository extends JpaRepository<Task, Long>
{
    List<Task> findByAssigneeId(Long assigneeId);
    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    Page<Task> findByProjectIdAndStatus(Long projectId, Status status, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.dueDate = :date")
    List<Task> findTasksDueOn(@Param("date") LocalDate date);
}
