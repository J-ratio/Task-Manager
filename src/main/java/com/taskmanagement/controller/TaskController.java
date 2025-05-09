package com.taskmanagement.controller;

import com.taskmanagement.dto.TaskDTO;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        task.setDueDate(taskDTO.getDueDate());
        
        if (taskDTO.getAssigneeId() != null) {
            return userRepository.findById(taskDTO.getAssigneeId())
                    .map(assignee -> {
                        task.setAssignee(assignee);
                        return ResponseEntity.ok(taskRepository.save(task));
                    })
                    .orElse(ResponseEntity.badRequest().build());
        }
        
        return ResponseEntity.ok(taskRepository.save(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        return taskRepository.findById(id)
                .map(existingTask -> {
                    existingTask.setTitle(taskDTO.getTitle());
                    existingTask.setDescription(taskDTO.getDescription());
                    existingTask.setStatus(taskDTO.getStatus());
                    existingTask.setPriority(taskDTO.getPriority());
                    existingTask.setDueDate(taskDTO.getDueDate());
                    
                    if (taskDTO.getAssigneeId() != null) {
                        return userRepository.findById(taskDTO.getAssigneeId())
                                .map(assignee -> {
                                    existingTask.setAssignee(assignee);
                                    return ResponseEntity.ok(taskRepository.save(existingTask));
                                })
                                .orElse(ResponseEntity.badRequest().build());
                    } else {
                        existingTask.setAssignee(null);
                        return ResponseEntity.ok(taskRepository.save(existingTask));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/assignee/{userId}")
    public List<Task> getTasksByAssignee(@PathVariable Long userId) {
        return taskRepository.findByAssigneeId(userId);
    }
} 