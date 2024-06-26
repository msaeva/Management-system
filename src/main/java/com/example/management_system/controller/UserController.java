package com.example.management_system.controller;

import com.example.management_system.domain.dto.user.ChangePasswordValidation;
import com.example.management_system.domain.dto.user.DetailedUserDTO;
import com.example.management_system.domain.dto.user.UpdateUserValidation;
import com.example.management_system.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/users")
public class UserController {

    @Inject
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/profile")
    @RolesAllowed({"USER", "PM"})
    public Response getAuthUser() {
        DetailedUserDTO profile = userService.getUserProfile();
        return Response.ok(profile).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/change-password")
    @RolesAllowed({"USER", "PM"})
    public Response changePassword(ChangePasswordValidation validation) {
        if (userService.changePassword(validation)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER", "PM"})
    public Response update(UpdateUserValidation validation) {
        DetailedUserDTO updated = userService.updateUser(validation);
        return Response.ok(updated).build();

    }
}
