package com.example.management_system.controller;

import com.example.management_system.domain.dto.TaskDTO;
import com.example.management_system.domain.dto.TaskValidation;
import com.example.management_system.service.TaskService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

}
