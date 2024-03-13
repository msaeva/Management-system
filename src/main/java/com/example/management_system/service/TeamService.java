package com.example.management_system.service;

import com.example.management_system.domain.dto.ProjectValidation;
import com.example.management_system.domain.dto.TeamValidation;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Team;
import com.example.management_system.repository.TeamRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class TeamService {

    @Inject
    private TeamRepository teamRepository;

    @Inject
    private ProjectService projectService;

    public Team create(TeamValidation validation) {
        Team team = new Team();
        team.setName(validation.getName());

        Project project = projectService.findById(validation.getProjectId());

        team.getProjects().add(project);


        // todo if validation user ids is not empty -> add it to the team
        return teamRepository.save(team);
    }
}
