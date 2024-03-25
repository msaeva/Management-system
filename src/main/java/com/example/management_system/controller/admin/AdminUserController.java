package com.example.management_system.controller.admin;

import com.example.management_system.domain.dto.user.RegisterUserValidation;
import com.example.management_system.domain.dto.user.SimpleUserDTO;
import com.example.management_system.domain.dto.user.UpdateUserValidation;
import com.example.management_system.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/admin")
public class AdminUserController {
    @Inject
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    @RolesAllowed({"ADMIN"})
    public Response getAll() {
        return Response.ok(userService.getALl()).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users/{id}")
    @RolesAllowed({"ADMIN"})
    public Response delete(@PathParam("id") Long id) {
        if (userService.deleteById(id)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(RegisterUserValidation validation) {
        SimpleUserDTO createdUser = userService.create(validation);
        return Response.status(Response.Status.CREATED)
                .entity(createdUser)
                .build();
    }

    @PUT
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(UpdateUserValidation validation) {
        userService.update(validation);
        return Response.ok().build();
    }
}
