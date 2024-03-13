package com.example.management_system.service;

import com.example.management_system.domain.dto.TaskDTO;
import com.example.management_system.domain.dto.TaskValidation;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Task;
import com.example.management_system.domain.entity.Team;
import com.example.management_system.domain.entity.User;
import com.example.management_system.repository.TaskRepository;
import com.example.management_system.service.mapper.TaskMapper;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Set;

@Stateless
public class TaskService {

    @Inject
    public TaskRepository taskRepository;

    @Inject
    public UserService userService;

    @Inject
    public TaskMapper taskMapper;

    @Inject
    public ProjectService projectService;

    public TaskDTO create(TaskValidation validation) {
        Task task = taskMapper.toTask(validation);

        Project project = projectService.findById(validation.getProjectId());

        Set<Team> teams = project.getTeams();

        User userToAdd = userService.findById(validation.getUserId());

        boolean isUserInTeam = teams.stream().anyMatch(t -> t.getId() == userToAdd.getId());

        if (!isUserInTeam) {
            // TODO throw custom exception
        }

        task.setProject(project);
        task.setUser(userToAdd);
        Task saved = taskRepository.save(task);

        return taskMapper.toDTO(saved);
    }
}
