package com.example.management_system.service;

import com.example.management_system.controller.errors.ProjectNotFoundException;
import com.example.management_system.domain.dto.ProjectValidation;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.User;
import com.example.management_system.repository.ProjectRepository;
import com.example.management_system.service.mapper.ProjectMapper;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class ProjectService {

    @Inject
    public ProjectRepository projectRepository;

    @Inject
    public AuthService authService;
    @Inject
    public ProjectMapper projectMapper;

    public Project create(ProjectValidation validation) {
        Project project = projectMapper.toProject(validation);
        return projectRepository.save(project);
    }

    public Project findById(Long id) {
        return projectRepository
                .findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id : " + id + " not founf!"));
    }

    public List<Project> getUserProjects() {
        User authUser = authService.getAuthenticatedUser();
        if (authUser == null) {
            // TODO throw exception
        }
        return projectRepository.getProjectsByUserId(authUser.getId());
    }
}
