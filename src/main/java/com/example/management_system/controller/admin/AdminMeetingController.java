package com.example.management_system.controller.admin;

import com.example.management_system.domain.dto.meeting.CreateMeetingValidation;
import com.example.management_system.domain.dto.meeting.PrivateMeetingDTO;
import com.example.management_system.domain.dto.meeting.UpdateMeetingValidation;
import com.example.management_system.service.MeetingService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/admin/meetings")
public class AdminMeetingController {
    @Inject
    private MeetingService meetingService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response filterMeetings(@QueryParam("userId") Long userId,
                                   @QueryParam("projectId") Long projectId) {

        List<PrivateMeetingDTO> meetings;

        if (userId != null && projectId != null) {
            meetings = meetingService.getByUserIdAndProjectId(userId, projectId);
        } else if (userId != null) {
            meetings = meetingService.getByUserId(userId);
        } else if (projectId != null) {
            meetings = meetingService.getByProjectId(projectId);
        } else {
            meetings = meetingService.getAll();
        }
        return Response.ok(meetings).build();
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response update(@PathParam("id") Long id, UpdateMeetingValidation validation) {
        return Response.ok(meetingService.update(id, validation))
                .build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response delete(@PathParam("id") Long id) {
        if (meetingService.delete(id)) {
            return Response.ok()
                    .build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN"})
    public Response create(CreateMeetingValidation validation) {
        PrivateMeetingDTO meeting = meetingService.create(validation);
        return Response.ok(meeting).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @DELETE()
    @Path("/{meetingId}/teams/{teamId}")
    @RolesAllowed({"ADMIN"})
    public Response removeTeamFromMeeting(@PathParam("teamId") Long teamId,
                                          @PathParam("meetingId") Long meetingId) {
        PrivateMeetingDTO updated = meetingService.removeTeamFromMeeting(teamId, meetingId);
        return Response.ok(updated).build();
    }
}
