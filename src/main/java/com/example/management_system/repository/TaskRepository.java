package com.example.management_system.repository;

import com.example.management_system.domain.entity.Task;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Stateless
public class TaskRepository {
    @PersistenceContext
    private EntityManager entityManager;


    public Task save(Task task) {
        try {
            entityManager.persist(task);
            entityManager.flush();
            entityManager.refresh(task);
            return task;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    public Optional<Task> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        String jpql = "SELECT t FROM Task t WHERE t.id = :id and t.deleted = false";
        TypedQuery<Task> query = entityManager.createQuery(jpql, Task.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    public boolean deleteByProjectId(Long id) {
        String jpql = "UPDATE Task t SET t.deleted = true WHERE t.project.id = :id";

        int deletedCount = entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
        return deletedCount > 0;
    }

    public boolean deleteById(Long id) {
        String jpql = "DELETE FROM Task t WHERE t.deleted = false and t.id = :id";
        int deletedCount = entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
        return deletedCount > 0;
    }

    public List<Task> getAllProjectTasks(Long projectId, int page, int size, String sort, String order) {
        String jpql = "SELECT t FROM Task t WHERE t.deleted = false and t.project.id = :projectId ORDER BY t." + sort + " " + order;
        Query query = entityManager.createQuery(jpql);
        query.setParameter("projectId", projectId);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public long getTaskCountByProjectId(Long projectId) {
        String queryString = "SELECT COUNT(t) FROM Task t WHERE t.deleted = false and t.project.id = :projectId";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("projectId", projectId);
        return (Long) query.getSingleResult();
    }

    public List<Task> getAllByProject(long projectId) {
        String jpql = "SELECT t FROM Task t WHERE t.deleted = false and t.project.id = :projectId";
        TypedQuery<Task> query = entityManager.createQuery(jpql, Task.class);
        query.setParameter("projectId", projectId);

        return query.getResultList();
    }
}
