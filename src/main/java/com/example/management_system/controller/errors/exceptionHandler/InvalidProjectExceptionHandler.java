package com.example.management_system.controller.errors.exceptionHandler;

import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.controller.errors.ProjectNotFoundException;
import com.example.management_system.controller.errors.UserNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidProjectExceptionHandler implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException ex) {
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String message = "An unexpected error occurred.";

        if (ex instanceof ProjectNotFoundException) {
            status = Response.Status.NOT_FOUND.getStatusCode();
            message = ex.getMessage();
//        } else if (ex instanceof InvalidUserException) {
//            status = Response.Status.BAD_REQUEST.getStatusCode();
//            message = ex.getMessage();
        }

        return Response.status(status).entity(message).build();
    }
}
