package com.example.management_system.controller;

import com.example.management_system.config.security.AuthenticationStoreImpl;
import com.example.management_system.domain.dto.user.LoginUserValidation;
import com.example.management_system.domain.dto.user.RegisterUserValidation;
import com.example.management_system.domain.dto.user.SimpleUserDTO;
import com.example.management_system.service.AuthService;
import com.example.management_system.service.UserService;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Path("/")
public class AccountController {
    @Inject
    private UserService userService;
    @Inject
    private AuthService authService;
    @Inject
    private AuthenticationStoreImpl authenticationStore;

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(RegisterUserValidation validation) {
        SimpleUserDTO createdUser = userService.create(validation);
        return Response.status(Response.Status.CREATED)
                .entity(createdUser)
                .build();
    }

    @POST
    @Path("authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(LoginUserValidation validation) {
        String username = validation.getUsername();
        String password = validation.getPassword();

        try {
            CredentialValidationResult validationResult =
                    authenticationStore.validate(new UsernamePasswordCredential(username, password));
            if (validationResult.getStatus() == CredentialValidationResult.Status.VALID) {
                String jwtToken = authService.getJwtToken(username);
                return Response.ok().entity(jwtToken).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials.").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(400).build();
    }
}
