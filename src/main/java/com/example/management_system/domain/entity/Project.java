package com.example.management_system.domain.entity;

import com.example.management_system.domain.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "PROJECTS")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRIMARY_PROJECT_ID")
    @SequenceGenerator(name = "SEQ_PRIMARY_PROJECT_ID", sequenceName = "SEQ_PRIMARY_PROJECT_ID", allocationSize = 1)
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private ProjectStatus status;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "ABBREVIATION")
    private String abbreviation;

    @ManyToMany
    @JoinTable(
            name = "PROJECTS_TEAMS",
            joinColumns = @JoinColumn(name = "PROJECT_ID"),
            inverseJoinColumns = @JoinColumn(name = "TEAM_ID")
    )
    private Set<Team> teams = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "PM_PROJECTS",
            joinColumns = @JoinColumn(name = "PROJECT_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
    private Set<User> pms = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Task> tasks;

    @Column(name = "DELETED")
    private boolean deleted;


    public Project(String title,
                   String description,
                   ProjectStatus status,
                   LocalDateTime createdDate,
                   String abbreviation,
                   Set<User> pms) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
        this.abbreviation = abbreviation;
        this.pms = pms;
    }
}
