package com.example.management_system.controller.pm;

import com.example.management_system.domain.dto.task.TaskDTO;
import com.example.management_system.domain.dto.user.PrivateSimpleUserDTO;
import com.example.management_system.service.ProjectService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/project-manager/projects")
public class PMProjectController {

    @Inject
    private ProjectService projectService;

    @RolesAllowed("PM")
    @Produces(MediaType.APPLICATION_JSON)
    @GET()
    @Path("/teams")
    public Response getPMProjectsWithTeams() {
        return Response.ok(projectService.getAllPMProjectsWithTeams()).build();
    }

    @RolesAllowed("PM")
    @Produces("application/json")
    @GET()
    @Path("/user")
    public Response getProjects() {
        return Response.ok(projectService.getProjects()).build();
    }

    @GET
    @Path("/{projectId}/tasks")
    @RolesAllowed({"PM"})
    @Produces("application/json")
    public Response getAllByTaskByProjectManager(@PathParam("projectId") long projectId) {
        List<TaskDTO> tasks = projectService.getAllByTaskByProjectManager(projectId);
        return Response.ok(tasks).build();
    }

    @GET
    @Path("/{projectId}/users")
    @RolesAllowed({"PM"})
    @Produces("application/json")
    public Response getAllUsersInProject(@PathParam("projectId") long projectId) {
        List<PrivateSimpleUserDTO> tasks = projectService.getAllUsersInProject(projectId);
        return Response.ok(tasks).build();
    }

}
