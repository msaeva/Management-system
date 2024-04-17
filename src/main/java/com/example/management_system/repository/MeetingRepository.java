package com.example.management_system.repository;

import com.example.management_system.domain.entity.Meeting;
import com.example.management_system.domain.entity.Project;
import com.example.management_system.domain.entity.Team;
import com.example.management_system.domain.entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;

@Stateless
public class MeetingRepository {
    @PersistenceContext
    private EntityManager entityManager;


    public Optional<Meeting> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Meeting meeting = entityManager.find(Meeting.class, id);
        return Optional.ofNullable(meeting);
    }

    public Meeting save(Meeting meeting) {
        try {
            entityManager.persist(meeting);
            entityManager.flush();
            entityManager.refresh(meeting);
            return meeting;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meeting;
    }

    public List<Meeting> findByUserAndProject(Long projectId, Long userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Meeting> query = criteriaBuilder.createQuery(Meeting.class);
        Root<Meeting> meetingRoot = query.from(Meeting.class);

        Join<Meeting, Team> teamsJoin = meetingRoot.join("teams");
        Join<Team, User> usersJoin = teamsJoin.join("users");
        Join<Meeting, Project> projectJoin = meetingRoot.join("project");

        query.select(meetingRoot).distinct(true);
        query.where(
                criteriaBuilder.equal(projectJoin.get("id"), projectId),
                criteriaBuilder.equal(usersJoin.get("id"), userId)
        );

        return entityManager.createQuery(query).getResultList();
    }

    public List<Meeting> getByProjectId(Long projectId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Meeting> query = criteriaBuilder.createQuery(Meeting.class);
        Root<Meeting> meetingRoot = query.from(Meeting.class);

        Join<Meeting, Project> projectJoin = meetingRoot.join("project");

        query.select(meetingRoot).distinct(true);
        query.where(
                criteriaBuilder.equal(projectJoin.get("id"), projectId)
        );

        return entityManager.createQuery(query).getResultList();
    }

    public List<Meeting> getAll() {
        String jpql = "SELECT m FROM Meeting m";
        TypedQuery<Meeting> query = entityManager.createQuery(jpql, Meeting.class);
        return query.getResultList();
    }

    public boolean deleteById(Long id) {
        String jpql = "DELETE FROM Meeting m WHERE m.id = :id";
        int deletedCount = entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
        return deletedCount > 0;
    }

    public List<Meeting> getByUserId(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Meeting> query = criteriaBuilder.createQuery(Meeting.class);
        Root<Meeting> meetingRoot = query.from(Meeting.class);

        Join<Meeting, Team> teamsJoin = meetingRoot.join("teams");
        Join<Team, User> usersJoin = teamsJoin.join("users");

        query.select(meetingRoot).distinct(true);
        query.where(criteriaBuilder.equal(usersJoin.get("id"), id));

        return entityManager.createQuery(query).getResultList();
    }

    public List<Meeting> getPMMeetingsById(Long id) {
        String jpql = "SELECT m " +
                "FROM Meeting m " +
                "JOIN m.project p " +
                "JOIN p.pms ppm " +
                "WHERE ppm.id = :userId";
        TypedQuery<Meeting> query = entityManager.createQuery(jpql, Meeting.class)
                .setParameter("userId", id);

        return query.getResultList();
    }

    public boolean deleteByProject(Long id) {
        String jpql = "DELETE FROM Meeting m WHERE m.project.id = :id";
        int deletedCount = entityManager.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
        return deletedCount >= 0;
    }
}
