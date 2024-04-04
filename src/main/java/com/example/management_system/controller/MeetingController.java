package com.example.management_system.controller;

import com.example.management_system.service.MeetingService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/meetings")
public class MeetingController {

    @Inject
    private MeetingService meetingService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER"})
    public Response getAuthUserMeetings() {
        return Response.ok(meetingService.getAuthUserMeetings())
                .build();
    }
}
