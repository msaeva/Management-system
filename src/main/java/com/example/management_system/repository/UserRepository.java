package com.example.management_system.repository;

import com.example.management_system.domain.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

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

    public List<User> findAllByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return List.of();
        }

        String jpql = "SELECT u FROM User u WHERE u.id IN :userIds and u.deleted = false";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("userIds", userIds);

        return query.getResultList();
    }

    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        String jpql = "SELECT u FROM User u WHERE u.id = :id and u.deleted = false";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    public Optional<User> findByUsername(String username) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username and u.deleted = false", User.class);
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
                    "SELECT u FROM User u WHERE u.email = :email and u.deleted = false", User.class);
            query.setParameter("email", email);
            User user = query.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    public List<User> findAll(int page, int size, String sort, String order) {
        String jpql;

        if (sort.equals("role")) {
            jpql = "SELECT u FROM User u JOIN u.role r WHERE u.deleted = false ORDER BY r.role " + order;
        } else if (sort.equals("projects")) {
            jpql = "SELECT DISTINCT u FROM User u LEFT JOIN u.teams t left join t.projects p " +
                    "WHERE u.deleted = false ORDER BY p.title " + order;
        } else {
            jpql = "SELECT u FROM User u where u.deleted = false ORDER BY u." + sort + " " + order;
        }

        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public List<User> findAllByRoleIds(List<Long> roleIds) {
        String jpql = "SELECT u FROM User u WHERE u.role.id  IN :ids and u.deleted = false";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("ids", roleIds);

        return query.getResultList();
    }

    public long getTotalCount() {
        String jpql = "select count(u) from User u where u.deleted = false";
        Query query = entityManager.createQuery(jpql);
        return (Long) query.getSingleResult();
    }

    public List<User> getAllUsersByProject(long projectId, int page, int size, String search) {
        String jpql = "SELECT DISTINCT u " +
                "FROM Project p " +
                "JOIN p.teams t " +
                "JOIN t.users u " +
                "WHERE p.id = :projectId " +
                "AND p.deleted = false " +
                "AND t.deleted = false " +
                "AND u.deleted = false ";

        if (search != null && !search.isEmpty()) {
            jpql += "AND (LOWER(u.firstName) LIKE LOWER(:search) OR LOWER(u.lastName) LIKE LOWER(:search)) ";
        }

        Query query = entityManager.createQuery(jpql, User.class)
                .setParameter("projectId", projectId);

        if (search != null && !search.isEmpty()) {
            query.setParameter("search", "%" + search.toLowerCase() + "%");
        }

        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    public Long getCountAllUsersByProject(Long id, String search) {
        String jpql = "SELECT count(distinct u.id) " +
                "FROM Project p " +
                "JOIN p.teams t " +
                "JOIN t.users u " +
                "WHERE p.id = :projectId " +
                "AND p.deleted = false " +
                "AND t.deleted = false " +
                "AND u.deleted = false ";

        if (search != null && !search.isEmpty()) {
            jpql += "AND (LOWER(u.firstName) LIKE LOWER(:search) OR LOWER(u.lastName) LIKE LOWER(:search)) ";
        }

        Query query = entityManager.createQuery(jpql, User.class)
                .setParameter("projectId", id);

        if (search != null && !search.isEmpty()) {
            query.setParameter("search", "%" + search.toLowerCase() + "%");
        }

        return (Long) query.getSingleResult();
    }
}
