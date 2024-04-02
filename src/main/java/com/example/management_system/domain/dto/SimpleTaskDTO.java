package com.example.management_system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimpleTaskDTO {
    private Long id;
    private String title;
    private String abbreviation;
    private String status;
}
