package com.example.management_system.domain.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Long projectId;
    private Long userId;
    private String userFullName;
    private String abbreviation;
    private Integer estimationTime;
    private Integer completionTime;
    private LocalDateTime createdDate;
    private Integer progress;
}
