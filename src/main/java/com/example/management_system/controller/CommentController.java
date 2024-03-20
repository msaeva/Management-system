package com.example.management_system.controller;

import com.example.management_system.domain.dto.CommentDTO;
import com.example.management_system.domain.dto.CommentValidation;
import com.example.management_system.service.CommentService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/comments")
public class CommentController {
    @Inject
    private CommentService commentService;

    @RolesAllowed({"USER", "PM"})
    @Produces("application/json")
    @POST()
    public Response create(CommentValidation validation) {
        CommentDTO comment = commentService.create(validation);
        return Response.ok(comment).build();
    }

    @RolesAllowed({"USER"})
    @Produces("application/json")
    @Path("/{id}")
    @DELETE
    public Response remove(@PathParam("id") long id) {
        if (commentService.remove(id)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @RolesAllowed({"USER"})
    @Produces("application/json")
    @Path("/{id}")
    @PUT
    public Response update(@PathParam("id") long id, String comment) {
        CommentDTO updated = commentService.update(id, comment);
        return Response.ok(updated).build();
    }
}
