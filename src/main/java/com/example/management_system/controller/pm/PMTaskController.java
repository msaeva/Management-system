package com.example.management_system.controller.pm;

import com.example.management_system.domain.dto.task.TaskDTO;
import com.example.management_system.domain.dto.task.TaskValidation;
import com.example.management_system.domain.dto.task.UpdateTaskValidation;
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

    @PUT
    @Path("/{id}")
    @RolesAllowed({"PM"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, UpdateTaskValidation validation) {
        TaskDTO updated = taskService.update(id, validation);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"PM"})
    public Response delete(@PathParam("id") Long id) {
        if (taskService.deleteById(id)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
