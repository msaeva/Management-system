package com.example.management_system.controller;

import com.example.management_system.domain.dto.team.TeamValidation;
import com.example.management_system.domain.entity.Team;
import com.example.management_system.service.TeamService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/teams")
public class TeamController {

    @Inject
    private TeamService teamService;

    @Produces(MediaType.APPLICATION_JSON)
    @POST()
    @RolesAllowed({"ADMIN", "PM"})
    public Response create(TeamValidation validation) {
        Team team = teamService.create(validation);
        return Response.ok(team).build();
    }
}
