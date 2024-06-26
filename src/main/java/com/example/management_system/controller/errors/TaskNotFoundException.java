package com.example.management_system.controller.errors;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;

@ApplicationException
public class TaskNotFoundException extends WebApplicationException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}