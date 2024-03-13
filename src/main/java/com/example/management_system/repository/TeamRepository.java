package com.example.management_system.repository;

import com.example.management_system.domain.entity.Team;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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

}
