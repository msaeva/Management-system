package com.example.management_system.service;

import com.example.management_system.controller.errors.CommentNotFoundException;
import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.domain.dto.CommentDTO;
import com.example.management_system.domain.dto.CommentValidation;
import com.example.management_system.domain.dto.PublicCommentDTO;
import com.example.management_system.domain.entity.Comment;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Task;
import com.example.management_system.domain.entity.User;
import com.example.management_system.domain.enums.Role;
import com.example.management_system.repository.CommentRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Stateless
public class CommentService {
    @Inject
    private TaskService taskService;
    @Inject
    private AuthService authService;
    @Inject
    private CommentRepository commentRepository;

    public CommentDTO create(CommentValidation validation) {
        User authUser = authService.getAuthenticatedUser();
        Task task = taskService.findById(validation.getTaskID());
        Project project = task.getProject();

        if (authUser.getRole().getRole() == Role.USER) {
            boolean userIsMember = project.getTeams().stream()
                    .anyMatch(team -> team.getUsers().contains(authUser));

            if (!userIsMember) {
                throw new InvalidUserException("User is not authorized to post a comment in this task.");
            }
        } else if (authUser.getRole().getRole() == Role.PM) {
            boolean isAuthUserPM = project.getPms().stream().anyMatch(u -> Objects.equals(u.getId(), authUser.getId()));

            if (!isAuthUserPM) {
                throw new InvalidUserException("User is not authorized to post a comment in this task.");
            }
        }

        Comment comment = new Comment();
        comment.setComment(validation.getComment());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setTask(task);
        comment.setAuthor(authUser);

        Comment saved = commentRepository.save(comment);

        return mapToCommentToDTO(saved, authUser, task);
    }

    public CommentDTO mapToCommentToDTO(Comment comment, User authUser, Task task) {
        return new CommentDTO(
                comment.getId(),
                comment.getComment(),
                authUser.getFirstName() + " " + authUser.getLastName(),
                authUser.getId(),
                comment.getCreatedDate().toString(),
                task.getId());
    }


    public Comment findById(Long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id: " + id + " not found!"));
    }

    public boolean remove(long id) {
        User authUser = authService.getAuthenticatedUser();
        if (authUser == null) {
            throw new InvalidUserException("User is not authenticated");
        }

        Comment comment = findById(id);

        if (!Objects.equals(comment.getAuthor().getId(), authUser.getId())) {
            throw new InvalidUserException("User with id:" + authUser.getId() + " is not owner of the comment with id: " + id);
        }

        comment.setDeleted(true);
        try {
            commentRepository.save(comment);
            return true;
        } catch (Exception e) {
            throw new CommentNotFoundException("Failed to mark comment with id " + id + " as deleted");
        }
    }

    public CommentDTO update(long id, String newComment) {
        User authUser = authService.getAuthenticatedUser();
        if (authUser == null) {
            throw new InvalidUserException("User is not authenticated");
        }
        Comment comment = findById(id);
        if (!Objects.equals(comment.getAuthor().getId(), authUser.getId())) {
            throw new InvalidUserException("User with id:" + authUser.getId() + " is not owner of the comment with id: " + id);
        }
        comment.setComment(newComment);
        Comment updated = commentRepository.save(comment);
        return new CommentDTO(
                updated.getId(),
                updated.getComment(),
                authUser.getFirstName() + " " + authUser.getLastName(),
                updated.getAuthor().getId(),
                updated.getCreatedDate().toString());
    }

    public PublicCommentDTO toPublicCommentDTO(Comment comment) {
        return new PublicCommentDTO(comment.getId(),
                comment.getComment(),
                comment.getAuthor().getFirstName() + " " + comment.getAuthor().getLastName(),
                comment.getAuthor().getId(),
                comment.getCreatedDate(),
                comment.getTask().getId());
    }

    public boolean deleteCommentsByTaskId(Long id) {
        return commentRepository.deleteByTaskId(id);
    }

    public List<Comment> getCommentsByTaskId(Long id) {
        return commentRepository.findByTaskId(id);
    }
}
