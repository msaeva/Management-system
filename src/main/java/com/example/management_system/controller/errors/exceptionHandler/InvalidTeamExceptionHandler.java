package com.example.management_system.controller.errors.exceptionHandler;

import com.example.management_system.controller.errors.TeamNotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidTeamExceptionHandler implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException ex) {
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String message = "An unexpected error occurred.";

        if (ex instanceof TeamNotFoundException) {
            status = Response.Status.NOT_FOUND.getStatusCode();
            message = ex.getMessage();
//        } else if (ex instanceof InvalidUserException) {
//            status = Response.Status.BAD_REQUEST.getStatusCode();
//            message = ex.getMessage();
        }
        return Response.status(status).entity(message).build();
    }
}
