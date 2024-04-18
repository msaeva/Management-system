package com.example.management_system.domain.dto.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskValidation {
    @Size(min = 3)
    @NotNull
    private String title;

    @Size(min = 5)
    @NotNull
    private String description;

    private String number;

    @NotNull
    private Long projectId;

    @NotNull
    private Long userId;
}
