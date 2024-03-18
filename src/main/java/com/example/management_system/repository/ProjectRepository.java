package com.example.management_system.repository;

import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Team;
import com.example.management_system.domain.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.util.List;
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

    public List<Project> getProjectsByUserId(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> projectRoot = criteriaQuery.from(Project.class);

        Join<Project, Team> teamJoin = projectRoot.join("teams");

        Join<Team, User> usersJoin = teamJoin.join("users");

        criteriaQuery.select(projectRoot).distinct(true);
        criteriaQuery.where(criteriaBuilder.equal(usersJoin.get("id"), id));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
