package com.example.management_system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class DetailedTaskDTO {
    private Long id;
    private String title;
    private String description;
    private String abbreviation;
    private LocalDateTime createdDate;
    private String status;
}
