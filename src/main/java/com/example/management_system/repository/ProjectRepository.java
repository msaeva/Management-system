package com.example.management_system.repository;

import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Team;
import com.example.management_system.domain.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
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
        if (id == null) {
            return Optional.empty();
        }
        String jpql = "SELECT p FROM Project p WHERE p.id = :id and p.deleted = false";
        TypedQuery<Project> query = entityManager.createQuery(jpql, Project.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    public List<Project> getProjectsByUserId(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Root<Project> projectRoot = criteriaQuery.from(Project.class);

        Join<Project, Team> teamJoin = projectRoot.join("teams");

        Join<Team, User> usersJoin = teamJoin.join("users");

        criteriaQuery.select(projectRoot).distinct(true);
        criteriaQuery.where(
                criteriaBuilder.equal(projectRoot.get("deleted"), false),
                criteriaBuilder.equal(usersJoin.get("id"), id));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }


    public List<Project> findAll() {
        return entityManager.createQuery("SELECT p FROM Project p where p.deleted = false", Project.class)
                .getResultList();
    }

    public boolean delete(Long id) {
        String jpql = "DELETE FROM Project p WHERE p.deleted = false and p.id = :id";
        int deletedCount = entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
        return deletedCount > 0;
    }

    public List<Project> getPMProjectsByUserId(Long id) {
        String jpql = "select p from Project p " +
                "join PMProject pm on pm.project.id = p.id " +
                "where p.deleted = false and pm.user.id = :id";

        TypedQuery<Project> query = entityManager.createQuery(jpql, Project.class);
        query.setParameter("id", id);

        return query.getResultList();
    }

    public Long findProjectByAbbreviation(String abbreviation) {
        String jpql = "select count(p) from Project p where p.deleted = false and lower(p.abbreviation) = lower(:abbreviation)";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("abbreviation", abbreviation.toLowerCase());

        return (Long) query.getSingleResult();
    }
}
