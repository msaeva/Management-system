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

    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u where u.deleted = false", User.class)
                .getResultList();
    }

    public List<User> findAll(int page, int size, String sort, String order) {
        String jpql = "SELECT u FROM User u where u.deleted = false ORDER BY u." + sort + " " + order;

        if (sort.equals("role")){
            jpql = "SELECT u FROM User u JOIN u.role r WHERE u.deleted = false ORDER BY r.role "  + order;
        }

        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
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

}
