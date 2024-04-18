package com.example.management_system.controller.errors.exceptionHandler;

import com.example.management_system.controller.errors.InvalidUserException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidUserExceptionMapper implements ExceptionMapper<InvalidUserException> {

    @Override
    public Response toResponse(InvalidUserException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ex.getMessage())
                .build();
    }
}