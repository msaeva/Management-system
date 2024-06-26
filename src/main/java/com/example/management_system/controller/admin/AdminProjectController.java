package com.example.management_system.controller.admin;

import com.example.management_system.domain.dto.Pagination;
import com.example.management_system.domain.dto.project.PrivateProjectDTO;
import com.example.management_system.domain.dto.project.ProjectValidation;
import com.example.management_system.domain.dto.project.UpdateProjectValidation;
import com.example.management_system.domain.dto.task.DetailedTaskDTO;
import com.example.management_system.domain.dto.user.PrivateSimpleUserDTO;
import com.example.management_system.domain.dto.user.SimpleUserDTO;
import com.example.management_system.service.ProjectService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/admin/projects")
public class AdminProjectController {

    @Inject
    private ProjectService projectService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response getAll() {
        return Response.ok(projectService.getAll()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/tasks")
    @RolesAllowed({"ADMIN"})
    public Response getAllProjectsWithTasks() {
        return Response.ok(projectService.getAllProjectsWithTasks()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/tasks")
    @RolesAllowed({"ADMIN"})
    public Response getAllProjectTasks(@PathParam("id") Long id,
                                       @QueryParam("page") int page,
                                       @QueryParam("size") int pageSize,
                                       @QueryParam("sort") String sortField,
                                       @QueryParam("order") String sortOrder
    ) {
        Pagination<DetailedTaskDTO> pagination = projectService.getAllProjectTasks(id, page, pageSize, sortField, sortOrder);
        return Response.ok(pagination).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response get(@PathParam("id") Long id) {
        return Response.ok(projectService.getById(id)).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response update(UpdateProjectValidation validation, @PathParam("id") Long id) {
        return Response.ok(projectService.update(validation, id)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response delete(@PathParam("id") Long id) {
        if (projectService.deleteById(id)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @DELETE()
    @Path("/{projectId}/teams/{teamId}")
    @RolesAllowed({"ADMIN"})
    public Response deleteTeam(@PathParam("projectId") Long projectId,
                               @PathParam("teamId") Long teamId) {
        if (projectService.deleteTeamFromProject(teamId, projectId)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @RolesAllowed("ADMIN")
    @Produces("application/json")
    @POST()
    public Response create(ProjectValidation validation) {
        PrivateProjectDTO project = projectService.create(validation);
        return Response.ok(project).build();
    }

    @RolesAllowed("ADMIN")
    @Produces("application/json")
    @POST()
    @Path("/{id}/project-managers")
    public Response getPms(@PathParam("id") Long id) {
        List<PrivateSimpleUserDTO> project = projectService.getProjectManagers(id);
        return Response.ok(project).build();
    }

    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    @PUT()
    @Path("/{projectId}/project-managers")
    public Response addProjectManager(@PathParam("projectId") Long projectId,
                                      List<Long> userIds) {
        List<SimpleUserDTO> projectManagers = projectService.addProjectManagers(projectId, userIds);
        return Response.ok(projectManagers).build();
    }

    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE()
    @Path("/{projectId}/project-managers/{pmId}")
    public Response removeProjectManager(@PathParam("projectId") Long projectId,
                                         @PathParam("pmId") Long pmId) {
        List<SimpleUserDTO> projectManagers = projectService.removeProjectManager(projectId, pmId);
        return Response.ok(projectManagers).build();
    }

    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    @GET()
    @Path("/users")
    public Response getAllProjectsWithUsers() {
        return Response.ok(projectService.getAllProjectsWithUsers()).build();
    }

    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    @GET()
    @Path("/teams")
    public Response getAllProjectsWithTeams() {
        return Response.ok(projectService.getAllProjectsWithTeams()).build();
    }

    @RolesAllowed("ADMIN")
    @GET()
    @Path("/exists")
    public Response checkAbbreviation(@QueryParam("abbreviation") String abbreviation) {
        boolean exists = projectService.checkIfAbbreviationExists(abbreviation);
        JsonObject json = Json.createObjectBuilder()
                .add("exists", exists)
                .build();
        return Response.ok().entity(json).build();
    }
}
