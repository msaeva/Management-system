package com.example.management_system.domain.entity;

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
@Table(name = "MEETINGS")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRIMARY_MEETING_ID")
    @SequenceGenerator(name = "SEQ_PRIMARY_MEETING_ID", sequenceName = "SEQ_PRIMARY_MEETING_ID", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "START_DATE")
    private LocalDateTime startDate;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID", referencedColumnName = "ID")
    private Project project;

    @ManyToMany
    @JoinTable(
            name = "MEETINGS_TEAMS",
            joinColumns = @JoinColumn(name = "MEETING_ID"),
            inverseJoinColumns = @JoinColumn(name = "TEAM_ID")
    )
    private Set<Team> teams = new HashSet<>();

    @Column(name = "DELETED")
    private boolean deleted;


    public Meeting(String title, LocalDateTime startDate, LocalDateTime endDate, Project project, Set<Team> teams) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = project;
        this.teams = teams;
    }
}
