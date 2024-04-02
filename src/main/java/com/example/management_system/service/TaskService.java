package com.example.management_system.service;

import com.example.management_system.controller.errors.InvalidTaskException;
import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.controller.errors.TaskNotFoundException;
import com.example.management_system.domain.dto.*;
import com.example.management_system.domain.entity.Comment;
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

    public DetailedTaskDTO create(TaskValidation validation) {

        Project project = projectService.findById(validation.getProjectId());
        Task task = new Task(validation.getTitle(),
                validation.getDescription(),
                TaskStatus.TODO.name(),
                LocalDateTime.now(),
                project,
                project.getAbbreviation() + "-",
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
        task.setAbbreviation(project.getAbbreviation() + "-" + savedTask.getId());
        taskRepository.save(savedTask);
        return mapToDetailedTaskDTO(savedTask);
    }

    public List<TaskDTO> getTasksByProjectId(long projectId) {
        User authUser = authService.getAuthenticatedUser();
        if (authUser == null) {
            throw new InvalidUserException("User is not authenticated");
        }
        return taskRepository
                .getTasksByUserAndProject(authUser.getId(), projectId)
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
        Long assignedUser = task.getUser() != null ? task.getUser().getId() : null;
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getProject().getId(),
                assignedUser,
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
        return this.mapToTaskDTO(task);
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

    public boolean deleteByProjectId(Long id) {
        return taskRepository.deleteByProjectId(id);
    }

    public List<PublicCommentDTO> getTaskComments(Long taskId) {
        Task task = findById(taskId);

        Set<Comment> comments = task.getComments();
        return comments
                .stream().map(c -> commentService.toPublicCommentDTO(c)).collect(Collectors.toList());
    }

    public void update(UpdateTaskValidation validation) {
        Task task = findById(validation.getId());
        task.setStatus(TaskStatus.valueOf(validation.getStatus()).name());
        task.setDescription(validation.getDescription());
        task.setTitle(validation.getTitle());
        task.setAbbreviation(validation.getAbbreviation());
        taskRepository.save(task);
    }

    public boolean deleteById(Long id) {
        Task task = findById(id);
        return taskRepository.deleteById(task.getId());
    }

    public List<DetailedTaskDTO> getAllProjectTasks(Long projectId, int page, int size, String sort, String sortOrder) {
        return taskRepository.getAllProjectTasks(projectId, page, size, sort, sortOrder)
                .stream()
                .map(this::mapToDetailedTaskDTO)
                .collect(Collectors.toList());
    }

    public long getTasksCountByProjectId(Long projectId) {
        return taskRepository.getTaskCountByProjectId(projectId);
    }
}
