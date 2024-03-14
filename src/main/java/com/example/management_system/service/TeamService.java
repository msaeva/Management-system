package com.example.management_system.service;

import com.example.management_system.controller.UserController;
import com.example.management_system.domain.dto.TeamValidation;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Team;
import com.example.management_system.domain.entity.User;
import com.example.management_system.repository.TeamRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.Set;
import java.util.logging.Logger;

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
}
