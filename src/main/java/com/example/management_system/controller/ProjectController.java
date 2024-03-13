package com.example.management_system.controller;

import com.example.management_system.domain.dto.ProjectValidation;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.service.ProjectService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/projects")
public class ProjectController {
    @Inject
    private ProjectService projectService;

    @Produces("application/json")
    @POST()
    public Response create(ProjectValidation validation) {
        Project project = projectService.create(validation);
        return Response.ok(project).build();
    }
}
