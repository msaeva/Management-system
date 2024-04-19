package com.example.management_system.repository;

import com.example.management_system.domain.entity.Comment;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;
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


    public Optional<Comment> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        String jpql = "SELECT c FROM Comment c WHERE c.id = :id and c.deleted = false";
        TypedQuery<Comment> query = entityManager.createQuery(jpql, Comment.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    public boolean deleteByTaskId(Long id) {
        String jpql = "UPDATE Comment c SET c.deleted = false WHERE c.task.id = :id";
        int deletedCount = entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
        return deletedCount > 0;
    }

    public List<Comment> findByTaskId(Long id) {
        if (id == null) {
            return Collections.emptyList();
        }
        String jpql = "SELECT c FROM Comment c WHERE c.deleted = false and c.task.id = :id";
        TypedQuery<Comment> query = entityManager.createQuery(jpql, Comment.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
