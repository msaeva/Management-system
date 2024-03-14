package com.example.management_system.controller;

import com.example.management_system.service.UserService;
import jakarta.inject.Inject;

import java.util.logging.Logger;

//@Path("/users")
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Inject
    private UserService userService;

}
