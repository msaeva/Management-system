package com.example.management_system.service;

import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.controller.errors.ProjectNotFoundException;
import com.example.management_system.domain.dto.TaskDTO;
import com.example.management_system.domain.dto.project.DetailedProjectDTO;
import com.example.management_system.domain.dto.project.ProjectValidation;
import com.example.management_system.domain.dto.project.SimpleProjectDTO;
import com.example.management_system.domain.dto.team.DetailedTeamDTO;
import com.example.management_system.domain.dto.user.SimpleUserDTO;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.User;
import com.example.management_system.domain.enums.ProjectStatus;
import com.example.management_system.domain.enums.Role;
import com.example.management_system.repository.ProjectRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class ProjectService {

    @Inject
    public ProjectRepository projectRepository;

    @Inject
    public AuthService authService;


    @Inject
    public TaskService taskService;

    @Inject
    public TeamService teamService;

    @Inject
    public UserService userService;

    public Project create(ProjectValidation validation) {
        User pm = userService.findById(validation.getPmId());
        if (pm.getRole().getRole() != Role.PM) {
            // TODO throw exception
        }
        Project project = new Project(validation.getTitle(),
                validation.getDescription(),
                ProjectStatus.STARTED,
                LocalDateTime.now(),
                validation.getAbbreviation(),
                Set.of(pm));
        return projectRepository.save(project);
    }

    public Project findById(Long id) {
        return projectRepository
                .findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id : " + id + " not found!"));
    }

    public List<SimpleProjectDTO> getUserProjects() {
        User authUser = authService.getAuthenticatedUser();
        if (authUser == null) {
            throw new InvalidUserException("User is not authenticated");
        }
        List<Project> projects = projectRepository.getProjectsByUserId(authUser.getId());
        return projects.stream().map(this::toSimpleProjectDTO).collect(Collectors.toList());
    }

    public SimpleProjectDTO toSimpleProjectDTO(Project project) {
        return new SimpleProjectDTO(project.getId(), project.getTitle());
    }

    public List<TaskDTO> getProjectTasks(long projectId) {
        return taskService.getTasksByProjectId(projectId);
    }

    public DetailedProjectDTO getById(Long id) {

        Project project = findById(id);
        List<DetailedTeamDTO> teams = project
                .getTeams().stream().map(t -> teamService.mapToDetailedTeamDTO(t))
                .collect(Collectors.toList());

        List<SimpleUserDTO> pms = project.getPms()
                .stream()
                .map(u -> userService.mapToSimpleUserDTO(u))
                .collect(Collectors.toList());

        return new DetailedProjectDTO(project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getStatus(),
                project.getCreatedDate(),
                teams, pms);
    }
}
