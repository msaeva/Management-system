package com.example.management_system.controller.pm;

import com.example.management_system.domain.dto.task.TaskDTO;
import com.example.management_system.domain.dto.task.TaskValidation;
import com.example.management_system.service.TaskService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/project-manager/tasks")
public class PMTaskController {
    @Inject
    private TaskService taskService;

    @Produces(MediaType.APPLICATION_JSON)
    @POST()
    @RolesAllowed({"PM"})
    public Response create(TaskValidation validation) {
        TaskDTO taskDTO = taskService.create(validation);
        return Response.ok(taskDTO).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/users/{userId}")
    @PUT
    @RolesAllowed({"PM"})
    public Response assignUser(@PathParam("id") long id, @PathParam("userId") long userId) {
        TaskDTO taskDTO = taskService.assignUserToTask(id, userId);
        return Response.ok(taskDTO).build();
    }
}
