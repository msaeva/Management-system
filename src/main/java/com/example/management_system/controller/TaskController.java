package com.example.management_system.controller;

import com.example.management_system.domain.dto.TaskDTO;
import com.example.management_system.domain.dto.TaskValidation;
import com.example.management_system.domain.entity.Task;
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
    @POST()
    @RolesAllowed({"ADMIN", "PM"})
    public Response create(TaskValidation validation) {
        TaskDTO taskDTO = taskService.create(validation);
        return Response.ok(taskDTO).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{taskId}")
    @PUT
    @RolesAllowed({"PM", "USER"})
    public Response update(@PathParam("taskId") long taskId, String status) {
        String updateStatus = taskService.updateStatus(taskId, status);
        return Response.ok(updateStatus).build();
    }
}
