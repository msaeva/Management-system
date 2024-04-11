package com.example.management_system.controller.pm;

import com.example.management_system.domain.dto.meeting.CreateMeetingValidation;
import com.example.management_system.domain.dto.meeting.PublicMeetingDTO;
import com.example.management_system.domain.dto.meeting.UpdateMeetingValidation;
import com.example.management_system.service.MeetingService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/project-manager/meetings")
public class PMMeetingController {
    @Inject
    private MeetingService meetingService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"PM"})
    public Response getAuthUserMeetings() {
        return Response.ok(meetingService.getAuthUserMeetings())
                .build();
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"PM"})
    public Response create(CreateMeetingValidation validation) {
        PublicMeetingDTO meeting = meetingService.create(validation);
        return Response.ok(meeting).build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"PM"})
    public Response update(@PathParam("id") Long id, UpdateMeetingValidation validation) {
        return Response.ok(meetingService.update(id, validation))
                .build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"PM"})
    public Response delete(@PathParam("id") Long id) {
        if (meetingService.delete(id)) {
            return Response.ok()
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
