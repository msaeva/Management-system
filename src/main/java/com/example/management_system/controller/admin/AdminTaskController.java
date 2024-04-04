package com.example.management_system.controller.admin;

import com.example.management_system.domain.dto.task.DetailedTaskDTO;
import com.example.management_system.domain.dto.task.TaskValidation;
import com.example.management_system.domain.dto.task.UpdateTaskValidation;
import com.example.management_system.service.TaskService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin/tasks")
public class AdminTaskController {

    @Inject
    private TaskService taskService;

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(UpdateTaskValidation validation) {
        taskService.update(validation);
        return Response.ok().build();
    }


    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response delete(@PathParam("id") Long id) {
        if (taskService.deleteById(id)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @POST()
    @RolesAllowed({"ADMIN"})
    public Response create(TaskValidation validation) {
        DetailedTaskDTO taskDTO = taskService.create(validation);
        return Response.ok(taskDTO).build();
    }
}
