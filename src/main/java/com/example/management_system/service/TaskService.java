package com.example.management_system.service;

import com.example.management_system.controller.errors.InvalidTaskException;
import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.controller.errors.TaskNotFoundException;
import com.example.management_system.domain.dto.TaskDTO;
import com.example.management_system.domain.dto.TaskValidation;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Task;
import com.example.management_system.domain.entity.User;
import com.example.management_system.domain.enums.TaskStatus;
import com.example.management_system.repository.TaskRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class TaskService {
    @Inject
    public TaskRepository taskRepository;

    @Inject
    public UserService userService;

    @Inject
    public ProjectService projectService;
    @Inject
    public AuthService authService;

    public TaskDTO create(TaskValidation validation) {
        Project project = projectService.findById(validation.getProjectId());
        Task task = new Task(validation.getTitle(),
                validation.getDescription(),
                TaskStatus.TODO.name(),
                LocalDateTime.now(),
                project,
                project.getAbbreviation() + "-" + validation.getNumber(),
                validation.getNumber());

        if (validation.getUserId() != null) {
            User userToAdd = userService.findById(validation.getUserId());

            if (project.getTeams().stream().anyMatch(team -> team.getUsers().contains(userToAdd))) {
                task.setUser(userToAdd);
                task.setStatus(TaskStatus.OPEN.name());
            } else {
                throw new InvalidUserException("User is not in any team of the project.");
            }
        } else {
            task.setUser(null);
        }

        Task savedTask = taskRepository.save(task);
        return toDTO(savedTask);
    }

    public List<TaskDTO> getTasksByProjectId(long projectId) {
        User authUser = authService.getAuthenticatedUser();
        if (authUser == null) {
            throw new InvalidUserException("User is not authenticated");
        }
        return taskRepository
                .getTasksByUserAndProject(authUser.getId(), projectId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TaskDTO toDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getProject().getId(),
                task.getUser().getId(),
                task.getAbbreviation());
    }

    public String updateStatus(long id, String status) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus(TaskStatus.valueOf(status).name());
            taskRepository.save(task);
            return task.getStatus();
        } else {
            throw new TaskNotFoundException("Task with id: " + id + " not found!");
        }
    }

    public TaskDTO getById(Long id) {
        Task task = findById(id);
        return this.toDTO(task);
    }

    public Task findById(Long id) {
        return taskRepository
                .findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " not found!"));
    }

    public void assignUserToTask(long id, long userId) {
        User user = userService.findById(userId);
        Task task = findById(id);

        if (task.getUser() != null) {
            throw new InvalidTaskException("Task with id: " + id + " already has assigned user");
        }

        task.setUser(user);
        taskRepository.save(task);
    }
}
