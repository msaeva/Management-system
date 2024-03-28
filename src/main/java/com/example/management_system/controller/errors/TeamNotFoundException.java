package com.example.management_system.controller.errors;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;

@ApplicationException
public class TeamNotFoundException extends WebApplicationException {
    public TeamNotFoundException(String message) {
        super(message);
    }
}
