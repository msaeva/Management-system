package com.example.management_system.controller;

import com.example.management_system.domain.dto.TeamValidation;
import com.example.management_system.domain.entity.Team;
import com.example.management_system.service.TeamService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/teams")
public class TeamController {

    @Inject
    private TeamService teamService;

    @Produces("application/json")
    @POST()
    public Response create(TeamValidation validation) {
        Team team = teamService.create(validation);
        return Response.ok(team).build();
    }
}
