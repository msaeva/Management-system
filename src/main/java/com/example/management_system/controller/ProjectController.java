package com.example.management_system.controller;

import com.example.management_system.domain.dto.task.TaskDTO;
import com.example.management_system.service.ProjectService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/projects")
public class ProjectController {
    @Inject
    private ProjectService projectService;

    @Produces("application/json")
    @GET()
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        return Response.ok(projectService.getById(id)).build();
    }

    @Produces("application/json")
    @GET()
    @Path("/user")
    @RolesAllowed("USER")
    public Response getUserProjects() {
        return Response.ok(projectService.getUserProjects()).build();
    }

    @GET
    @Path("/{projectId}/tasks")
    @RolesAllowed({"USER"})
    public Response getTasksForUserTeamsByProjectId(@PathParam("projectId") long projectId) {
        List<TaskDTO> tasks = projectService.getTasksForUserTeamsByProjectId(projectId);
        return Response.ok(tasks).build();
    }
}
