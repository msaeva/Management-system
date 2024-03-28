package com.example.management_system.controller.admin;

import com.example.management_system.domain.dto.team.DetailedTeamDTO;
import com.example.management_system.domain.dto.team.TeamValidation;
import com.example.management_system.domain.dto.user.SimpleUserDTO;
import com.example.management_system.service.TeamService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/admin/teams")
public class AdminTeamController {

    @Inject
    private TeamService teamService;

    @Produces(MediaType.APPLICATION_JSON)
    @POST()
    @RolesAllowed({"ADMIN"})
    public Response create(TeamValidation validation) {
        DetailedTeamDTO team = teamService.create(validation);
        return Response.ok(team).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @PUT()
    @Path("/{teamId}/users/")
    @RolesAllowed({"ADMIN"})
    public Response addUserToTeam(@PathParam("teamId") Long teamId,
                                  List<Long> userIds) {
        List<SimpleUserDTO> addedUsers = teamService.addUserToTeam(teamId, userIds);
        return Response.ok(addedUsers).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @DELETE()
    @Path("/{teamId}/users/{userId}")
    @RolesAllowed({"ADMIN"})
    public Response removeUserFromTeam(@PathParam("teamId") Long teamId,
                                       @PathParam("userId") Long userId) {
        if (teamService.removeUserFromTeam(teamId, userId)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
