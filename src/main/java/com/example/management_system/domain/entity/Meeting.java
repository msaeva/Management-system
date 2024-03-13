package com.example.management_system.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "MEETINGS")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRIMARY_MEETING_ID")
    @SequenceGenerator(name = "SEQ_PRIMARY_MEETING_ID", sequenceName = "SEQ_PRIMARY_MEETING_ID", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "MEETING_DATETIME")
    private LocalDateTime meetingDateTime;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID", referencedColumnName = "ID")
    private Project project;
}
