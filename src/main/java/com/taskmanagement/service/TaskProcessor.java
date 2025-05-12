package com.taskmanagement.service;

import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskPriority;
import com.taskmanagement.entity.TaskStatus;
import com.taskmanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskProcessor {

    private final TaskRepository taskRepository;
    private final PriorityBlockingQueue<Task> taskQueue = new PriorityBlockingQueue<>(
            100,
            Comparator.comparing(Task::getPriority)
                    .thenComparing(Task::getCreatedAt)
    );

    @Value("${task.processor.interval:60000}")
    private long processingInterval;

    @Scheduled(fixedRateString = "${task.processor.interval:60000}")
    public void processTasks() {
        log.info("Starting task processing cycle");
        
        // Load new tasks into the queue
        List<Task> newTasks = taskRepository.findByStatus(TaskStatus.TODO);
        taskQueue.addAll(newTasks);
        
        // Process tasks from the queue
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            if (task != null) {
                processTask(task);
            }
        }
        
        log.info("Completed task processing cycle");
    }

    private void processTask(Task task) {
        try {
            log.info("Processing task: {} with priority: {}", task.getTitle(), task.getPriority());
            
            // Simulate task processing
            Thread.sleep(1000); // Simulate work
            
            // Update task status
            task.setStatus(TaskStatus.IN_PROGRESS);
            taskRepository.save(task);
            
            // Simulate more work
            Thread.sleep(2000); // Simulate more work
            
            // Mark task as completed
            task.setStatus(TaskStatus.DONE);
            taskRepository.save(task);
            
            log.info("Completed processing task: {}", task.getTitle());
        } catch (InterruptedException e) {
            log.error("Task processing interrupted for task: {}", task.getTitle(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Error processing task: {}", task.getTitle(), e);
            task.setStatus(TaskStatus.TODO); // Reset status on error
            taskRepository.save(task);
        }
    }

    public void addTaskToQueue(Task task) {
        taskQueue.offer(task);
        log.info("Added task to queue: {} with priority: {}", task.getTitle(), task.getPriority());
    }
} 