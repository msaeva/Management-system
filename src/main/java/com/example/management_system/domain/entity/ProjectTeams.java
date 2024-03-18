package com.example.management_system.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "PROJECTS_TEAMS")
public class ProjectTeams {

    @Id
    @ManyToOne
    @JoinColumn(name = "PROJECT_ID", referencedColumnName = "ID")
    private Project project;

    @Id
    @ManyToOne
    @JoinColumn(name = "TEAM_ID", referencedColumnName = "ID")
    private Team team;
}
