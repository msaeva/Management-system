package com.example.management_system.controller;

import com.example.management_system.domain.dto.RegisterUserValidation;
import com.example.management_system.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Path("/users")
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Inject
    private UserService userService;

    @Produces("application/json")
    @POST()
    public Response create(RegisterUserValidation validation) {

        LOGGER.info("contr " + validation.toString());
//        LOGGER.info(" info " + dto.toString());
//        User user1 = new User();
//        user1.setEmail(dto.getEmail());
//        user1.setFirstName(dto.getFirstName());
//        user1.setLastName(dto.getLastName());
//        user1.setPassword(dto.getPassword());

//        LOGGER.info(user1.toString());
        userService.create(validation);
        return Response.ok("User created successfully").build();
    }
}
