package com.taskmanagement.service;

import com.taskmanagement.dto.TaskDTO;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskStatus;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskProcessor taskProcessor;

    @Transactional
    public Task createTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(TaskStatus.TODO); // Set initial status as TODO
        task.setPriority(taskDTO.getPriority());
        task.setDueDate(taskDTO.getDueDate());

        if (taskDTO.getAssigneeId() != null) {
            userRepository.findById(taskDTO.getAssigneeId())
                    .ifPresent(task::setAssignee);
        }

        Task savedTask = taskRepository.save(task);
        // Add the task to the processor queue
        taskProcessor.addTaskToQueue(savedTask);
        return savedTask;
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByAssignee(Long assigneeId) {
        return taskRepository.findByAssigneeId(assigneeId);
    }

    @Transactional
    public Task updateTaskStatus(Long taskId, TaskStatus newStatus) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setStatus(newStatus);
                    Task updatedTask = taskRepository.save(task);
                    
                    // If the task is set back to TODO, add it to the processor queue
                    if (newStatus == TaskStatus.TODO) {
                        taskProcessor.addTaskToQueue(updatedTask);
                    }
                    
                    return updatedTask;
                })
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
    }

    @Transactional
    public void reprocessFailedTasks() {
        List<Task> failedTasks = taskRepository.findByStatus(TaskStatus.TODO);
        log.info("Reprocessing {} failed tasks", failedTasks.size());
        failedTasks.forEach(taskProcessor::addTaskToQueue);
    }
} 