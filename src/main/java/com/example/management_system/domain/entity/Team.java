package com.example.management_system.domain.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "TEAMS")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRIMARY_TEAM_ID")
    @SequenceGenerator(name = "SEQ_PRIMARY_TEAM_ID", sequenceName = "SEQ_PRIMARY_TEAM_ID", allocationSize = 1)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "USERS_TEAMS",
            joinColumns = @JoinColumn(name = "TEAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
    private Set<User> users = new HashSet<>();

    @JsonbTransient
    @ManyToMany
    @JoinTable(
            name = "PROJECTS_TEAMS",
            joinColumns = @JoinColumn(name = "TEAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "PROJECT_ID")
    )
    private Set<Project> projects = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "MEETINGS_TEAMS",
            joinColumns = @JoinColumn(name = "TEAM_ID"),
            inverseJoinColumns = @JoinColumn(name = "MEETING_ID")
    )
    private Set<Meeting> meetings = new HashSet<>();

    @Column(name = "DELETED")
    private boolean deleted;


}
