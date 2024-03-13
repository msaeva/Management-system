package com.example.management_system.repository;

import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Task;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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
}
