package com.example.management_system.controller.errors.exceptionHandler;

import com.example.management_system.controller.errors.TaskNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidTaskExceptionHandler implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException ex) {
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String message = "An unexpected error occurred.";

        if (ex instanceof TaskNotFoundException) {
            status = Response.Status.NOT_FOUND.getStatusCode();
            message = ex.getMessage();
        }
        return Response.status(status).entity(message).build();
    }
}
