package com.example.management_system.domain.dto.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskValidation {
    @NotNull
    private Long id;

    @Size(min = 3)
    @NotNull
    private String title;

    @NotNull
    private String abbreviation;

    @Size(min = 3)
    @NotNull
    private String description;

    @NotNull
    private String status;

    @NotNull
    private Long userId;
}
