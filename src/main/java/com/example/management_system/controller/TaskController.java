package com.example.management_system.controller;

import com.example.management_system.domain.dto.PublicCommentDTO;
import com.example.management_system.domain.dto.task.TaskDTO;
import com.example.management_system.domain.dto.task.UpdateTaskValidation;
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
    @Path("/{taskId}/status")
    @PUT
    @RolesAllowed({"PM", "USER"})
    public Response update(@PathParam("taskId") long taskId, String status) {
        TaskDTO updated = taskService.updateStatus(taskId, status);
        return Response.ok(updated).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"USER"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, UpdateTaskValidation validation) {
        TaskDTO updated = taskService.update(id, validation);
        return Response.ok(updated).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @GET
    @RolesAllowed({"PM", "USER"})
    public Response get(@PathParam("id") long id) {
        TaskDTO taskDTO = taskService.getById(id);
        return Response.ok(taskDTO).build();
    }

    @RolesAllowed({"USER", "PM"})
    @Produces("application/json")
    @Path("/{taskId}/comments")
    @GET
    public Response getComments(@PathParam("taskId") Long taskId) {
        List<PublicCommentDTO> comments = taskService.getTaskComments(taskId);
        return Response.ok(comments).build();
    }
}
