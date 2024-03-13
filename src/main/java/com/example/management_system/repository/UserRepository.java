package com.example.management_system.repository;

import com.example.management_system.domain.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;
import java.util.Set;

//@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
public class UserRepository {


    @PersistenceContext
    private EntityManager entityManager;


    public void save(User user) {
        try {
            entityManager.persist(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> findByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of(); // Return an empty list if the input list is null or empty
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
}
