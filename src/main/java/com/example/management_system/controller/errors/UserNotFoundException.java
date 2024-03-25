package com.example.management_system.controller.errors;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;
@ApplicationException
public class UserNotFoundException extends WebApplicationException {
    public UserNotFoundException(String message) {
        super(message);
    }

}