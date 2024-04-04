package com.example.management_system.controller.errors;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;

@ApplicationException
public class CommentNotFoundException extends WebApplicationException {
    public CommentNotFoundException(String message) {
        super(message);
    }
}
