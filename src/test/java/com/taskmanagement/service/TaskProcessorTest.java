package com.taskmanagement.service;

import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskPriority;
import com.taskmanagement.entity.TaskStatus;
import com.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TaskProcessorTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testTaskProcessing() throws InterruptedException {
        // Create a test task
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setPriority(TaskPriority.HIGH);
        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(LocalDateTime.now());
        
        // Save the task
        Task savedTask = taskRepository.save(task);
        assertNotNull(savedTask.getId());
        assertEquals(TaskStatus.TODO, savedTask.getStatus());

        // Wait for processing (max 10 seconds)
        int maxAttempts = 10;
        int attempts = 0;
        while (attempts < maxAttempts) {
            Task updatedTask = taskRepository.findById(savedTask.getId()).orElse(null);
            if (updatedTask != null && updatedTask.getStatus() == TaskStatus.DONE) {
                break;
            }
            TimeUnit.SECONDS.sleep(1);
            attempts++;
        }

        // Verify the task was processed
        Task processedTask = taskRepository.findById(savedTask.getId()).orElse(null);
        assertNotNull(processedTask);
        assertEquals(TaskStatus.DONE, processedTask.getStatus());
    }

    @Test
    public void testPriorityBasedProcessing() throws InterruptedException {
        // Create tasks with different priorities
        Task lowPriorityTask = createAndSaveTask("Low Priority Task", TaskPriority.LOW);
        Task highPriorityTask = createAndSaveTask("High Priority Task", TaskPriority.HIGH);
        Task mediumPriorityTask = createAndSaveTask("Medium Priority Task", TaskPriority.MEDIUM);

        // Wait for processing (max 15 seconds)
        int maxAttempts = 15;
        int attempts = 0;
        while (attempts < maxAttempts) {
            if (areAllTasksProcessed(lowPriorityTask.getId(), mediumPriorityTask.getId(), highPriorityTask.getId())) {
                break;
            }
            TimeUnit.SECONDS.sleep(1);
            attempts++;
        }

        // Verify all tasks were processed
        assertTrue(areAllTasksProcessed(lowPriorityTask.getId(), mediumPriorityTask.getId(), highPriorityTask.getId()));
    }

    private Task createAndSaveTask(String title, TaskPriority priority) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription("Test Description");
        task.setPriority(priority);
        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    private boolean areAllTasksProcessed(Long... taskIds) {
        for (Long taskId : taskIds) {
            Task task = taskRepository.findById(taskId).orElse(null);
            if (task == null || task.getStatus() != TaskStatus.DONE) {
                return false;
            }
        }
        return true;
    }
} 