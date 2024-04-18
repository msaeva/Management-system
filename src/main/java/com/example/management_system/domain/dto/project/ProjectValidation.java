package com.example.management_system.domain.dto.project;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectValidation {
    @NotNull
    @Size(min = 3)
    private String title;

    @NotNull
    @Size(min = 5)
    private String description;

    @NotNull
    private String abbreviation;

    @NotNull
    private Long[] pmIds;
}
