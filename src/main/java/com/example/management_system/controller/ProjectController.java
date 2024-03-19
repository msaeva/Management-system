package com.example.management_system.controller;

import com.example.management_system.domain.dto.ProjectValidation;
import com.example.management_system.domain.dto.TaskDTO;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.service.ProjectService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/projects")
public class ProjectController {
    @Inject
    private ProjectService projectService;

    @RolesAllowed("ADMIN")
    @Produces("application/json")
    @POST()
    public Response create(ProjectValidation validation) {
        Project project = projectService.create(validation);
        return Response.ok(project).build();
    }

    @Produces("application/json")
    @GET()
    @Path("/user")
    public Response get() {
        return Response.ok(projectService.getUserProjects()).build();
    }

    @GET
    @Path("/{projectId}/tasks")
    @RolesAllowed({"ADMIN", "PM", "USER"})
    public Response getTasksByProjectId(@PathParam("projectId") long projectId) {
        List<TaskDTO> tasks = projectService.getProjectTasks(projectId);
        return Response.ok(tasks).build();
    }
}
