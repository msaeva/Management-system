package com.example.management_system.repository;

import com.example.management_system.domain.entity.Team;
import com.example.management_system.domain.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Stateless
public class TeamRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Team save(Team team) {
        try {
            entityManager.persist(team);
            entityManager.flush();
            entityManager.refresh(team);
            return team;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return team;
    }

    public Optional<Team> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Team team = entityManager.find(Team.class, id);
        return Optional.ofNullable(team);
    }

    public List<Team> findByIds(List<Long> teamIds) {
        if (teamIds == null || teamIds.isEmpty()) {
            return List.of();
        }

        String jpql = "SELECT t FROM Team t where t.id IN :teamIds";
        TypedQuery<Team> query = entityManager.createQuery(jpql, Team.class);
        query.setParameter("teamIds", teamIds);

        return query.getResultList();
    }
}
