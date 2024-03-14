package com.example.management_system;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@DeclareRoles({"ADMIN", "USER", "PM"})
@ApplicationPath("/api")
public class HelloApplication extends Application {

}