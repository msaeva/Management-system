package com.example.management_system.domain.dto.project;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectValidation {
    @NotNull
    @Size(min = 3)
    private String title;

    @NotNull @Size(min = 5)
    private String description;

    @NotNull
    private String abbreviation;

    @NotNull
    private String status;
}
