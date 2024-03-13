package com.example.management_system.repository;

import com.example.management_system.domain.entity.UserRole;
import com.example.management_system.domain.enums.Role;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

@Stateless
public class UserRoleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<UserRole> findByName(String name) {
        String query = "SELECT ur FROM UserRole ur WHERE ur.role = :name";
        TypedQuery<UserRole> typedQuery = entityManager.createQuery(query, UserRole.class);
        typedQuery.setParameter("name", Role.valueOf(name.toUpperCase()));

        try {
            return Optional.of(typedQuery.getSingleResult());
        } catch (Exception e) {
            return null;
        }
    }
}
