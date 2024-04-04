package com.example.management_system.service;

import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.controller.errors.MeetingNotFoundException;
import com.example.management_system.domain.dto.meeting.PrivateMeetingDTO;
import com.example.management_system.domain.dto.meeting.PublicMeetingDTO;
import com.example.management_system.domain.dto.meeting.UpdateMeetingValidation;
import com.example.management_system.domain.dto.user.PrivateSimpleUserDTO;
import com.example.management_system.domain.entity.Meeting;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.User;
import com.example.management_system.repository.MeetingRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class MeetingService {
    @Inject
    private MeetingRepository meetingRepository;
    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    @Inject
    private AuthService authService;

    public List<PublicMeetingDTO> getAuthUserMeetings() {
        User authUser = authService.getAuthenticatedUser();
        if (authUser == null) {
            throw new InvalidUserException("User is not authenticated");
        }
        return meetingRepository
                .getByUserId(authUser.getId())
                .stream()
                .map(this::mapToPublicMeetingDTO)
                .collect(Collectors.toList());
    }

    private PublicMeetingDTO mapToPublicMeetingDTO(Meeting meeting) {
        return new PublicMeetingDTO(meeting.getId(),
                meeting.getTitle(),
                meeting.getStatus(),
                Timestamp.valueOf(meeting.getStartDate()).getTime(),
                Timestamp.valueOf(meeting.getEndDate()).getTime()
        );
    }

    public Meeting findById(Long id) {
        return meetingRepository
                .findById(id)
                .orElseThrow(() -> new MeetingNotFoundException("Meeting with id :" + id + " not found!"));
    }

    public PublicMeetingDTO update(Long id, UpdateMeetingValidation validation) {
        Meeting meeting = findById(id);

        Timestamp start = new Timestamp(validation.getStart());
        Timestamp end = new Timestamp(validation.getEnd());
        meeting.setTitle(validation.getTitle());
        meeting.setStartDate(start.toLocalDateTime());
        meeting.setEndDate(end.toLocalDateTime());

        Meeting updated = meetingRepository.save(meeting);
        return mapToPublicMeetingDTO(updated);
    }

    public List<PrivateMeetingDTO> getByUserIdAndProjectId(Long userId, Long projectId) {
        Project project = projectService.findById(projectId);
        User user = userService.findById(userId);

        return meetingRepository
                .findByUserAndProject(project.getId(), user.getId())
                .stream()
                .map(this::mapToPrivateMeetingDTO)
                .collect(Collectors.toList());
    }

    private PrivateMeetingDTO mapToPrivateMeetingDTO(Meeting meeting) {
        List<PrivateSimpleUserDTO> users = meeting.getTeams()
                .stream()
                .flatMap(team -> team.getUsers().stream())
                .map(u -> userService.mapToPrivateSimpleUserDTO(u))
                .collect(Collectors.toList());

        return new PrivateMeetingDTO(meeting.getId(),
                meeting.getTitle(),
                meeting.getStatus(),
                Timestamp.valueOf(meeting.getStartDate()).getTime(),
                Timestamp.valueOf(meeting.getEndDate()).getTime(),
                users
        );
    }

    public List<PrivateMeetingDTO> getByUserId(Long userId) {
        User user = userService.findById(userId);
        return meetingRepository
                .getByUserId(user.getId())
                .stream()
                .map(this::mapToPrivateMeetingDTO)
                .collect(Collectors.toList());
    }

    public List<PrivateMeetingDTO> getByProjectId(Long projectId) {
        Project project = projectService.findById(projectId);
        return meetingRepository
                .getByProjectId(project.getId())
                .stream()
                .map(this::mapToPrivateMeetingDTO)
                .collect(Collectors.toList());
    }

    public List<PrivateMeetingDTO> getAll() {
        return meetingRepository.getAll()
                .stream()
                .map(this::mapToPrivateMeetingDTO)
                .collect(Collectors.toList());
    }

    public boolean delete(Long id) {
        return this.meetingRepository.deleteById(id);
    }
}
