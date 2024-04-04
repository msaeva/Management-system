package com.example.management_system.controller.errors;

import jakarta.ejb.ApplicationException;
@ApplicationException
public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(String message) {
        super(message);
    }
}
