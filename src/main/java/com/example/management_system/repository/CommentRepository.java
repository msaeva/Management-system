package com.example.management_system.repository;

import com.example.management_system.domain.entity.Comment;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

@Stateless
public class CommentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Comment save(Comment comment) {
        try {
            entityManager.persist(comment);
            entityManager.flush();
            entityManager.refresh(comment);
            return comment;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comment;
    }

    public boolean removeById(long id) {
        String jpql = "DELETE FROM Comment c WHERE c.id = :id";
        int deletedCount = entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
        return deletedCount > 0;

    }

    public Optional<Comment> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Comment project = entityManager.find(Comment.class, id);
        return Optional.ofNullable(project);
    }

    public boolean deleteByTaskId(Long id) {
        String jpql = "DELETE FROM Comment c WHERE c.task.id = :id";
        int deletedCount = entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
        return deletedCount > 0;
    }
}
