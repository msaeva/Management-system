package com.example.management_system.controller;

import com.example.management_system.domain.dto.PublicCommentDTO;
import com.example.management_system.domain.dto.TaskDTO;
import com.example.management_system.service.TaskService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/tasks")
public class TaskController {

    @Inject
    public TaskService taskService;

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{taskId}")
    @PUT
    @RolesAllowed({"PM", "USER"})
    public Response update(@PathParam("taskId") long taskId, String status) {
        String updateStatus = taskService.updateStatus(taskId, status);
        return Response.ok(updateStatus).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/users/{userId}")
    @PUT
    @RolesAllowed({"PM"})
    public Response assignUser(@PathParam("id") long id, @PathParam("userId") long userId) {
        taskService.assignUserToTask(id, userId);
        return Response.ok().build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @GET
    @RolesAllowed({"PM", "USER", "ADMIN"})
    public Response get(@PathParam("id") long id) {
        TaskDTO taskDTO = taskService.getById(id);
        return Response.ok(taskDTO).build();
    }

    @RolesAllowed({"USER"})
    @Produces("application/json")
    @Path("/{taskId}/comments")
    @GET
    public Response getComments(@PathParam("taskId") Long taskId) {
        List<PublicCommentDTO> comments = taskService.getTaskComments(taskId);
        return Response.ok(comments).build();
    }
}
