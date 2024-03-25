package com.example.management_system.controller.errors;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;
@ApplicationException
public class InvalidUserException extends WebApplicationException {
    public InvalidUserException(String message) {
        super(message);
    }
}
