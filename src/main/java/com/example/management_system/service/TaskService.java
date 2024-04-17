package com.example.management_system.service;

import com.example.management_system.controller.errors.InvalidTaskException;
import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.controller.errors.TaskNotFoundException;
import com.example.management_system.domain.dto.*;
import com.example.management_system.domain.dto.task.DetailedTaskDTO;
import com.example.management_system.domain.dto.task.TaskDTO;
import com.example.management_system.domain.dto.task.TaskValidation;
import com.example.management_system.domain.dto.task.UpdateTaskValidation;
import com.example.management_system.domain.entity.*;
import com.example.management_system.domain.enums.Role;
import com.example.management_system.domain.enums.TaskStatus;
import com.example.management_system.repository.TaskRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
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

    @Inject
    public CommentService commentService;
    private static final Logger LOGGER = Logger.getLogger(TaskService.class.getName());

    public TaskDTO create(TaskValidation validation) {

        Project project = projectService.findById(validation.getProjectId());
        Task task = new Task(validation.getTitle(),
                validation.getDescription(),
                TaskStatus.OPEN.name(),
                LocalDateTime.now(),
                project,
                project.getAbbreviation() + "-",
                validation.getNumber());

        if (validation.getUserId() != null) {
            User userToAdd = userService.findById(validation.getUserId());

            if (project.getTeams().stream().anyMatch(team -> team.getUsers().contains(userToAdd))) {
                task.setUser(userToAdd);
                task.setStatus(TaskStatus.TODO.name());
            } else {
                throw new InvalidUserException("User is not in any team of the project.");
            }
        } else {
            task.setUser(null);
        }
        Task savedTask = taskRepository.save(task);
        task.setAbbreviation(project.getAbbreviation() + "-" + savedTask.getId());
        Task updated = taskRepository.save(savedTask);

        return mapToTaskDTO(updated);
    }

    public List<TaskDTO> getTasksForUserTeamsByProjectId(long projectId) {
        User authUser = authService.getAuthenticatedUser();
        if (authUser == null) {
            throw new InvalidUserException("User is not authenticated");
        }

        Project project = projectService.findById(projectId);
        List<Team> teamsWithAuthUser = project.getTeams().stream()
                .filter(team -> team.getUsers().stream().anyMatch(u -> u.getId().equals(authUser.getId())))
                .collect(Collectors.toList());

        List<Long> userIds = teamsWithAuthUser.stream()
                .flatMap(t -> t.getUsers().stream())
                .map(User::getId).collect(Collectors.toList());

        List<Task> result = project.getTasks().stream()
                .filter(task -> {
                    User taskUser = task.getUser();
                    return taskUser == null || userIds.contains(taskUser.getId());
                }).collect(Collectors.toList());

        return result.stream().map(this::mapToTaskDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getAllTasksByProjectId(long projectId) {
        return taskRepository.getAllByProject(projectId)
                .stream()
                .map(this::mapToTaskDTO)
                .collect(Collectors.toList());

    }

    private DetailedTaskDTO mapToDetailedTaskDTO(Task task) {
        return new DetailedTaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getAbbreviation(),
                task.getCreatedDate(),
                task.getStatus());
    }

    private TaskDTO mapToTaskDTO(Task task) {
        Long userId = null;
        String userFullName = null;

        if (task.getUser() != null) {
            userFullName = task.getUser().getFirstName() + " " + task.getUser().getLastName();
            userId = task.getUser().getId();
        }

        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getProject().getId(),
                userId,
                userFullName,
                task.getAbbreviation(),
                task.getEstimationTime(),
                task.getCreatedDate(),
                task.getProgress());
    }

    public String updateStatus(long id, String status) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus(TaskStatus.valueOf(status).name());
            Task updated = taskRepository.save(task);
            return updated.getStatus();
        } else {
            throw new TaskNotFoundException("Task with id: " + id + " not found!");
        }
    }

    public TaskDTO getById(Long id) {
        Task task = findById(id);
        return this.mapToTaskDTO(task);
    }

    public Task findById(Long id) {
        return taskRepository
                .findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " not found!"));
    }

    public TaskDTO assignUserToTask(long id, long userId) {
        User user = userService.findById(userId);
        Task task = findById(id);

        if (task.getUser() != null) {
            throw new InvalidTaskException("Task with id: " + id + " already has assigned user");
        }

        task.setUser(user);
        task.setStatus(TaskStatus.TODO.name());
        Task updatedTask = taskRepository.save(task);

        return mapToTaskDTO(updatedTask);
    }

    public boolean deleteByProjectId(Long id) {
        return taskRepository.deleteByProjectId(id);
    }

    public List<PublicCommentDTO> getTaskComments(Long taskId) {
        Task task = findById(taskId);

        Set<Comment> comments = task.getComments();
        return comments
                .stream().map(c -> commentService.toPublicCommentDTO(c)).collect(Collectors.toList());
    }

    public TaskDTO update(Long id, UpdateTaskValidation validation) {
        Task task = findById(id);

        if (validation.getUserId() != null) {
            User user = userService.findById(validation.getUserId());
            task.setUser(user);
        }
        task.setStatus(TaskStatus.valueOf(validation.getStatus()).name());
        task.setDescription(validation.getDescription());
        task.setTitle(validation.getTitle());
        task.setAbbreviation(validation.getAbbreviation());
        Task saved = taskRepository.save(task);
        return mapToTaskDTO(saved);
    }

    public boolean deleteById(Long id) {
        Task task = findById(id);
        if (commentService.deleteCommentsByTaskId(task.getId())) {
            return taskRepository.deleteById(task.getId());
        }
        return false;
    }

    public List<DetailedTaskDTO> getAllProjectTasks(Long projectId, int page, int size, String sort, String order) {
        return taskRepository.getAllProjectTasks(projectId, page, size, sort, order)
                .stream()
                .map(this::mapToDetailedTaskDTO)
                .collect(Collectors.toList());
    }

    public long getTasksCountByProjectId(Long projectId) {
        return taskRepository.getTaskCountByProjectId(projectId);
    }

    public TaskDTO setEstimationTime(Long id, Integer estimationTime) {
        Task task = findById(id);
        if (estimationTime >= 1) {
            task.setEstimationTime(estimationTime);
        } else {
            throw new InvalidTaskException("Estimation time is invalid!");
        }
        Task updated = taskRepository.save(task);
        return mapToTaskDTO(updated);
    }

    public TaskDTO changeProgress(Long id, Integer progress) {
        Task task = findById(id);
        if (progress >= 0 && progress <= 100) {
            task.setProgress(progress);
        }
        Task saved = taskRepository.save(task);
        return mapToTaskDTO(saved);
    }
}
