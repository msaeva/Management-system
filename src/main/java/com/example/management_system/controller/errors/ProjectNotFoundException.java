package com.example.management_system.controller.errors;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;
@ApplicationException
public class ProjectNotFoundException extends WebApplicationException {
    public ProjectNotFoundException(String message) {
        super(message);
    }
}
