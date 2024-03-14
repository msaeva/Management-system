package com.example.management_system.repository;

import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@Stateless
public class ProjectRepository {
    @PersistenceContext
    private EntityManager entityManager;


    public Project save(Project project) {
        try {
            entityManager.persist(project);
            entityManager.flush();
            entityManager.refresh(project);
            return project;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return project;
    }

    public Optional<Project> findById(Long id) {
        Project project = entityManager.find(Project.class, id);
        return Optional.ofNullable(project);
    }
}
