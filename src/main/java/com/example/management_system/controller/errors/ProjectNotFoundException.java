package com.example.management_system.controller.errors;

public class ProjectNotFoundException extends RuntimeException{
    public ProjectNotFoundException(String message) {
        super(message);
    }
}
