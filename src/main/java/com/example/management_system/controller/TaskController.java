package com.example.management_system.controller;

import com.example.management_system.domain.dto.PublicCommentDTO;
import com.example.management_system.domain.dto.task.TaskDTO;
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
        TaskDTO updated = taskService.updateStatus(taskId, status);
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

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/estimation-time")
    @PUT
    @RolesAllowed({"USER"})
    public Response setEstimationTime(@PathParam("id") Long id, Integer estimationTime) {
        TaskDTO taskDTO = taskService.setEstimationTime(id, estimationTime);
        return Response.ok(taskDTO).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/completion-time")
    @PUT
    @RolesAllowed({"USER"})
    public Response setCompletionTime(@PathParam("id") Long id, Integer estimationTime) {
        TaskDTO taskDTO = taskService.setCompletionTime(id, estimationTime);
        return Response.ok(taskDTO).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/progress")
    @PUT
    @RolesAllowed({"USER"})
    public Response changeProgress(@PathParam("id") Long id, Integer progress) {
        TaskDTO taskDTO = taskService.changeProgress(id, progress);
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
