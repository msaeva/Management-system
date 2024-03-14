package com.example.management_system.repository;

import com.example.management_system.domain.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Stateless
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public User save(User user) {
        try {
            entityManager.persist(user);
            entityManager.flush();
            entityManager.refresh(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> findByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }

        String jpql = "SELECT u FROM User u WHERE u.id IN :userIds";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("userIds", userIds);

        return query.getResultList();
    }

    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.of(user);
    }

    public Optional<User> findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        List<User> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return Optional.of(resultList.get(0));
        } else {
            return Optional.empty();
        }
    }
}
