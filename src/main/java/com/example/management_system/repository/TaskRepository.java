package com.example.management_system.repository;

import com.example.management_system.domain.entity.Task;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    public List<Task> getTasksByUserAndProject(Long userId, long projectId) {
        String jpql = "SELECT t FROM Task t WHERE t.project.id = :projectId AND t.user.id = :userId";
        TypedQuery<Task> query = entityManager.createQuery(jpql, Task.class);
        query.setParameter("projectId", projectId);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public Optional<Task> findById(long id) {
        Task task = entityManager.find(Task.class, id);
        return Optional.ofNullable(task);
    }

    public boolean deleteByProjectId(Long id) {
        String jpql = "DELETE FROM Task t WHERE t.project.id = :id";
        int deletedCount = entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
        return deletedCount > 0;
    }
}
