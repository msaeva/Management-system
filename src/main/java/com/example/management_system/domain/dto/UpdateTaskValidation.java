package com.example.management_system.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskValidation {
    private Long id;
    private String title;
    private String abbreviation;
    private String description;
    private String status;
}
