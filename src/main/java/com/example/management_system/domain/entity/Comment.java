package com.example.management_system.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@RequiredArgsConstructor
@Entity
@Table(name = "COMMENTS")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRIMARY_COMMENT_ID")
    @SequenceGenerator(name = "SEQ_PRIMARY_COMMENT_ID", sequenceName = "SEQ_PRIMARY_COMMENT_ID", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User author;

    @ManyToOne
    @JoinColumn(name = "TASK_ID", referencedColumnName = "ID")
    private Task task;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
}
