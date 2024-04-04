package com.example.management_system.service;

import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.controller.errors.ProjectNotFoundException;
import com.example.management_system.domain.dto.*;
import com.example.management_system.domain.dto.project.*;
import com.example.management_system.domain.dto.task.DetailedTaskDTO;
import com.example.management_system.domain.dto.task.SimpleTaskDTO;
import com.example.management_system.domain.dto.task.TaskDTO;
import com.example.management_system.domain.dto.team.DetailedTeamDTO;
import com.example.management_system.domain.dto.user.PrivateSimpleUserDTO;
import com.example.management_system.domain.dto.user.SimpleUserDTO;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Task;
import com.example.management_system.domain.entity.Team;
import com.example.management_system.domain.entity.User;
import com.example.management_system.domain.enums.ProjectStatus;
import com.example.management_system.domain.enums.Role;
import com.example.management_system.repository.ProjectRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.*;
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

    public PrivateProjectDTO create(ProjectValidation validation) {
        Set<User> pms = new HashSet<>();
        for (Long pmId : validation.getPmIds()) {
            pms.add(userService.findById(pmId));
        }

        pms.removeIf(u -> u.getRole().getRole() != Role.PM);

        Project project = new Project(validation.getTitle(),
                validation.getDescription(),
                ProjectStatus.STARTED,
                LocalDateTime.now(),
                validation.getAbbreviation(),
                pms);

        Project saved = projectRepository.save(project);
        return mapToPrivateProjectDTO(saved);
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
        return projects.stream().map(this::mapToSimpleProjectDTO).collect(Collectors.toList());
    }

    public SimpleProjectDTO mapToSimpleProjectDTO(Project project) {
        return new SimpleProjectDTO(project.getId(), project.getTitle());
    }

    public List<TaskDTO> getProjectTasks(long projectId) {
        return taskService.getTasksByProjectId(projectId);
    }

    public DetailedProjectDTO getById(Long id) {
        Project project = findById(id);
        return mapToDetailedProjectDTO(project);
    }

    private DetailedProjectDTO mapToDetailedProjectDTO(Project project) {
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
                project.getAbbreviation(),
                project.getStatus(),
                project.getCreatedDate(),
                teams, pms);
    }

    public List<PrivateProjectDTO> getAll() {
        return projectRepository
                .findAll()
                .stream()
                .map(this::mapToPrivateProjectDTO)
                .collect(Collectors.toList());
    }

    public PrivateProjectDTO mapToPrivateProjectDTO(Project project) {
        return new PrivateProjectDTO(project.getId(), project.getTitle(), project.getDescription(), project.getAbbreviation(), project.getStatus().name());
    }

    public DetailedProjectDTO update(UpdateProjectValidation validation, Long id) {
        Project project = findById(id);

        project.setStatus(ProjectStatus.valueOf(validation.getStatus()));
        project.setDescription(validation.getDescription());
        project.setTitle(validation.getTitle());
        project.setTitle(validation.getTitle());
        project.setAbbreviation(validation.getAbbreviation());

        Project updated = projectRepository.save(project);

        return mapToDetailedProjectDTO(updated);
    }

    public boolean deleteById(Long id) {
        Project project = findById(id);
        taskService.deleteByProjectId(project.getId());
        return projectRepository.delete(project.getId());
    }

    public boolean deleteTeamFromProject(Long teamId, Long projectId) {
        Team team = teamService.findById(teamId);
        Project project = findById(projectId);

        if (team != null && project != null) {
            project.getTeams().removeIf(t -> Objects.equals(t.getId(), team.getId()));
            projectRepository.save(project);
            return true;
        } else {
            return false;
        }
    }

    public List<PrivateSimpleUserDTO> getProjectManagers(Long id) {
        Project project = findById(id);
        return project.getPms()
                .stream()
                .map(pm -> userService.mapToPrivateSimpleUserDTO(pm)).
                collect(Collectors.toList());
    }

    public List<SimpleUserDTO> addProjectManagers(Long projectId, List<Long> userIds) {
        Project project = findById(projectId);

        List<Long> newProjectManagerIds = userIds.stream()
                .filter(userId -> project.getPms().stream().noneMatch(pm -> pm.getId().equals(userId)))
                .collect(Collectors.toList());

        List<User> newProjectManagers = newProjectManagerIds.stream()
                .map(userService::findById)
                .collect(Collectors.toList());

        project.getPms().addAll(newProjectManagers);

        projectRepository.save(project);

        return project
                .getPms()
                .stream()
                .map(p -> userService.mapToSimpleUserDTO(p))
                .collect(Collectors.toList());
    }

    public List<SimpleUserDTO> removeProjectManager(Long projectId, Long pmId) {
        Project project = findById(projectId);
        User user = userService.findById(pmId);
        project.getPms().removeIf(pm -> Objects.equals(pm.getId(), user.getId()));

        projectRepository.save(project);
        return project
                .getPms()
                .stream()
                .map(p -> userService.mapToSimpleUserDTO(p))
                .collect(Collectors.toList());
    }

    public List<ProjectTaskDTO> getAllProjectsWithTasks() {
        return projectRepository
                .findAll()
                .stream()
                .map(this::mapToProjectTaskDTO)
                .collect(Collectors.toList());
    }

    private ProjectTaskDTO mapToProjectTaskDTO(Project p) {
        PrivateProjectDTO project = mapToPrivateProjectDTO(p);
        List<SimpleTaskDTO> tasks = p.getTasks()
                .stream()
                .map(this::mapToPrivateTaskDTO)
                .limit(5)
                .collect(Collectors.toList());

        return new ProjectTaskDTO(project, tasks);
    }

    private SimpleTaskDTO mapToPrivateTaskDTO(Task task) {
        return new SimpleTaskDTO(
                task.getId(),
                task.getTitle(),
                task.getAbbreviation(),
                task.getStatus()
        );
    }

    private DetailedTaskDTO mapToDetailedTaskDTO(Task t) {
        return new DetailedTaskDTO(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getAbbreviation(),
                t.getCreatedDate(),
                t.getStatus());
    }

    public Pagination<DetailedTaskDTO> getAllProjectTasks(Long projectId, int page, int size, String sort, String order) {
        List<DetailedTaskDTO> tasks = taskService.getAllProjectTasks(projectId, page, size, sort, order);
        long totalRecords = taskService.getTasksCountByProjectId(projectId);
        return new Pagination<>(tasks, totalRecords);
    }

    public List<ProjectUserDTO> getAllProjectsWithUsers() {
        List<Project> all = this.projectRepository.findAll();

        ArrayList<ProjectUserDTO> result = new ArrayList<>();
        for (Project project : all) {
            List<Long> userIds = project.getTeams().stream()
                    .flatMap(t -> t.getUsers().stream())
                    .map(User::getId).collect(Collectors.toList());

            result.add(new ProjectUserDTO(project.getId(), project.getTitle(), userIds));
        }
        return result;
    }
}
