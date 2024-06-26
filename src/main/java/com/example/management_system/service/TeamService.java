package com.example.management_system.service;

import com.example.management_system.controller.errors.TeamNotFoundException;
import com.example.management_system.domain.dto.team.DetailedTeamDTO;
import com.example.management_system.domain.dto.team.TeamValidation;
import com.example.management_system.domain.dto.user.SimpleUserDTO;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Team;
import com.example.management_system.domain.entity.User;
import com.example.management_system.repository.TeamRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class TeamService {

    @Inject
    private TeamRepository teamRepository;
    @Inject
    private UserService userService;
    @Inject
    private ProjectService projectService;

    public DetailedTeamDTO create(TeamValidation validation) {
        Team team = new Team();

        String initialName = validation.getName() != null && !validation.getName().isEmpty() ?
                validation.getName() : "Team - ";

        team.setName(initialName);

        Project project = projectService.findById(validation.getProjectId());
        team.getProjects().add(project);

        if (validation.getUserIds() != null && !validation.getUserIds().isEmpty()) {
            Set<User> existingUsers = team.getUsers();
            Set<User> usersToAdd = userService.findAllByIds(validation.getUserIds());

            usersToAdd.removeIf(existingUsers::contains);
            team.getUsers().addAll(usersToAdd);
        }

        Team saved = teamRepository.save(team);
        saved.setName("Team - " + saved.getId());

        Team updatedTeam = teamRepository.save(saved);

        return mapToDetailedTeamDTO(updatedTeam);
    }

    public DetailedTeamDTO mapToDetailedTeamDTO(Team team) {
        List<SimpleUserDTO> userDTOS = team.getUsers()
                .stream()
                .map(u -> userService.mapToSimpleUserDTO(u))
                .collect(Collectors.toList());

        return new DetailedTeamDTO(team.getId(), team.getName(), userDTOS);
    }

    public Team findById(Long id) {
        return teamRepository
                .findById(id)
                .orElseThrow(() -> new TeamNotFoundException("Team with id: " + id + " not found!"));
    }

    public List<SimpleUserDTO> addUserToTeam(Long teamId, List<Long> userIds) {
        Team team = findById(teamId);
        HashSet<User> usersToAdd = new HashSet<>();
        for (Long id : userIds) {
            usersToAdd.add(userService.findById(id));
        }

        team.getUsers().addAll(usersToAdd);
        Team saved = teamRepository.save(team);
        return saved
                .getUsers()
                .stream()
                .map(u -> userService.mapToSimpleUserDTO(u))
                .collect(Collectors.toList());
    }

    public boolean removeUserFromTeam(Long teamId, Long userId) {
        Team team = findById(teamId);
        User user = userService.findById(userId);

        if (team != null && user != null) {
            team.getUsers().removeIf(u -> Objects.equals(u.getId(), user.getId()));
            teamRepository.save(team);
            return true;
        }
        return false;
    }

    public Set<Team> findByIds(List<Long> teamIds) {
        return new HashSet<>(teamRepository.findByIds(teamIds));
    }

//    public boolean removeTeamFromMeeting(Long teamId, Long meetingId) {
//        Team team = findById(teamId);
//        Meeting meeting = meetingService.findById(meetingId);
//
//        Set<Team> updatedTeams = meeting.getTeams().stream()
//                .filter(t -> !t.getId().equals(teamId))
//                .collect(Collectors.toSet());
//
//        meeting.setTeams(updatedTeams);
//
//        return meetingService.mapToPrivateMeetingDTO(meeting);
//    }
}
