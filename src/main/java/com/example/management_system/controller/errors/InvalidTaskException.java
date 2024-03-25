package com.example.management_system.controller.errors;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;

@ApplicationException
public class InvalidTaskException extends WebApplicationException {
    public InvalidTaskException(String message) {
        super(message);
    }
}
