package com.example.management_system.domain.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PrivateProjectDTO {
    private Long id;
    private String title;
    private String description;
    private String abbreviation;
    private String status;
}
