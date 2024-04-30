package com.example.management_system.domain.dto.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskValidation {
    @Size(min = 3)
    @NotNull
    private String title;

    @Size(min = 3)
    @NotNull
    private String description;

    @NotNull
    private String status;

    @NotNull
    private Long userId;

    @NotNull
    @PositiveOrZero
    private Integer completionTime;

    @NotNull
    @PositiveOrZero
    private Integer estimationTime;

    @NotNull
    @PositiveOrZero
    private Integer progress;
}
