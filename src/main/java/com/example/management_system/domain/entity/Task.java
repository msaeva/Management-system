package com.example.management_system.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "TASKS")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRIMARY_TASK_ID")
    @SequenceGenerator(name = "SEQ_PRIMARY_TASK_ID", sequenceName = "SEQ_PRIMARY_TASK_ID", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "NUMBER")
    private String number;

    @Column(name = "ABBREVIATION")
    private String abbreviation;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID", referencedColumnName = "ID")
    private Project project;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    public Task(String title,
                String description,
                String status,
                LocalDateTime createdDate,
                Project project,
                String abbreviation,
                String number) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
        this.project = project;
        this.abbreviation = abbreviation;
        this.number = number;
    }
}
