package com.example.management_system.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "MEETINGS_TEAMS")
public class MeetingsTeams {
    @Id
    @ManyToOne
    @JoinColumn(name = "MEETING_ID", referencedColumnName = "ID")
    private Meeting meeting;

    @Id
    @ManyToOne
    @JoinColumn(name = "TEAM_ID", referencedColumnName = "ID")
    private Team team;
}
