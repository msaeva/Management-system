package com.example.management_system.service;

import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.domain.dto.TaskDTO;
import com.example.management_system.domain.dto.TaskValidation;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Task;
import com.example.management_system.domain.entity.User;
import com.example.management_system.repository.TaskRepository;
import com.example.management_system.service.mapper.TaskMapper;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

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

        if (validation.getUserId() != null) {
            User userToAdd = userService.findById(validation.getUserId());

            if (project.getTeams().stream().anyMatch(team -> team.getUsers().contains(userToAdd))) {
                task.setUser(userToAdd);
            } else {
                throw new InvalidUserException("User is not in any team of the project.");
            }

        } else {
            task.setUser(null);
        }

        task.setProject(project);
        Task savedTask = taskRepository.save(task);

        return taskMapper.toDTO(savedTask);
    }
}
