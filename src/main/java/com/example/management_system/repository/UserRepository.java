package com.example.management_system.repository;

import com.example.management_system.domain.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class UserRepository {
    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());
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

    public List<User> findAllByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }

        String jpql = "SELECT u FROM User u WHERE u.id IN :userIds";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("userIds", userIds);

        return query.getResultList();
    }

    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public Optional<User> findByUsername(String username) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    public List<User> findAll(int page, int size, String sort, String order) {
        String jpql = "SELECT u FROM User u ORDER BY u." + sort + " " + order;

        LOGGER.info("query " + jpql);
        LOGGER.info("page " + page);
        LOGGER.info("sort " + sort);
        LOGGER.info("sort " + sort);
        LOGGER.info("order " + order);

        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setFirstResult((page - 1) * size); // offset
        query.setMaxResults(size); // limit
        return query.getResultList();
    }

    public boolean deleteById(Long id) {
        String jpql = "DELETE FROM User u WHERE u.id = :id";
        int deletedCount = entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
        return deletedCount > 0;
    }

    public List<User> findAllByRoleIds(List<Long> roleIds) {
        String jpql = "SELECT u FROM User u WHERE u.role.id IN :ids";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("ids", roleIds);

        return query.getResultList();
    }

    public long getTotalCount() {
        String jpql = "select count(t) from User t";
        Query query = entityManager.createQuery(jpql);
        return (Long) query.getSingleResult();
    }

}
