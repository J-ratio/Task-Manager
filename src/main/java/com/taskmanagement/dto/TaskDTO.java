package com.taskmanagement.dto;

import com.taskmanagement.entity.TaskPriority;
import com.taskmanagement.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long assigneeId;
    private LocalDateTime dueDate;
} 