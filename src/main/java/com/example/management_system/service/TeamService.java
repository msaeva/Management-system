package com.example.management_system.service;

import com.example.management_system.controller.UserController;
import com.example.management_system.domain.dto.team.DetailedTeamDTO;
import com.example.management_system.domain.dto.team.TeamValidation;
import com.example.management_system.domain.dto.user.SimpleUserDTO;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Team;
import com.example.management_system.domain.entity.User;
import com.example.management_system.repository.TeamRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class TeamService {

    @Inject
    private TeamRepository teamRepository;
    @Inject
    private UserService userService;
    @Inject
    private ProjectService projectService;

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    public Team create(TeamValidation validation) {
        Team team = new Team();
        team.setName(validation.getName());
        Project project = projectService.findById(validation.getProjectId());

        team.getProjects().add(project);

        // TODO check if user is already added to the team
        if (validation.getUserIds() != null && !validation.getUserIds().isEmpty()) {
            Set<User> usersToAdd = userService.findAllByIds(validation.getUserIds());
            team.getUsers().addAll(usersToAdd);
        }
        return teamRepository.save(team);
    }

    public DetailedTeamDTO mapToDetailedTeamDTO(Team team) {
        List<SimpleUserDTO> userDTOS = team.getUsers()
                .stream()
                .map(u -> userService.mapToSimpleUserDTO(u))
                .collect(Collectors.toList());

        return new DetailedTeamDTO(team.getId(), team.getName(), userDTOS);
    }
}
