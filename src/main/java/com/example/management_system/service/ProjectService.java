package com.example.management_system.service;

import com.example.management_system.domain.dto.ProjectValidation;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.enums.ProjectStatus;
import com.example.management_system.repository.ProjectRepository;
import com.example.management_system.service.mapper.ProjectMapper;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

@Stateless
public class ProjectService {

    @Inject
    public ProjectRepository projectRepository;

    @Inject
    public ProjectMapper projectMapper;

    public Project create(ProjectValidation validation) {
        Project project = projectMapper.toProject(validation);
        return projectRepository.save(project);
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow();
    }
}
