package com.example.management_system.controller.admin;

import com.example.management_system.domain.dto.project.PrivateProjectDTO;
import com.example.management_system.domain.dto.project.ProjectValidation;
import com.example.management_system.domain.dto.project.UpdateProjectValidation;
import com.example.management_system.domain.dto.user.PrivateSimpleUserDTO;
import com.example.management_system.domain.dto.user.SimpleUserDTO;
import com.example.management_system.service.ProjectService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
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
}
