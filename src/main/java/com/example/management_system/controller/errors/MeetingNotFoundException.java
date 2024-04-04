package com.example.management_system.controller.errors;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;

@ApplicationException
public class MeetingNotFoundException extends WebApplicationException {
    public MeetingNotFoundException(String message) {
        super(message);
    }
}
