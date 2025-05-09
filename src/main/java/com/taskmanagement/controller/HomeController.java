package com.taskmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/") // Not working, returns 404
    public String home() {
        return "Welcome to Task Management System API!";
    }

    @GetMapping("/api")
    public String apiHome() {
        return "Welcome to Task Management System API!";
    }

    @GetMapping("/api/health")
    public String health() {
        return "Service is up and running!";
    }
} 