package com.example.management_system.service;

import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.domain.dto.CommentDTO;
import com.example.management_system.domain.dto.CommentValidation;
import com.example.management_system.domain.dto.PublicCommentDTO;
import com.example.management_system.domain.entity.Comment;
import com.example.management_system.domain.entity.Task;
import com.example.management_system.domain.entity.User;
import com.example.management_system.repository.CommentRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
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

        Comment comment = new Comment();
        comment.setComment(validation.getComment());
        comment.setCreatedDate(LocalDateTime.now());
        comment.setTask(task);
        comment.setAuthor(authUser);

        Comment saved = commentRepository.save(comment);

        return new CommentDTO(saved.getComment(),
                authUser.getUsername(),
                saved.getCreatedDate().toString(),
                task.getId());
    }

    public Comment findById(Long id) {
        // TODO throw custom exception
        return commentRepository.findById(id).orElseThrow();
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

        return commentRepository.removeById(id);
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
        return new CommentDTO(updated.getComment(),
                authUser.getUsername(),
                updated.getCreatedDate().toString());
    }

    public PublicCommentDTO toPublicCommentDTO(Comment comment) {
        return new PublicCommentDTO(comment.getId(),
                comment.getComment(),
                comment.getAuthor().getFirstName() + " " + comment.getAuthor().getLastName(),
                comment.getCreatedDate(),
                comment.getTask().getId());
    }
}
