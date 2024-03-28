package com.example.management_system.domain.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectValidation {
    private String title;
    private String description;
    private String abbreviation;
    private String status;
}
