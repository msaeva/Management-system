package com.example.management_system.domain.dto.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskValidation {
    private String title;
    private String description;
    private Long projectId;
    private Long userId;
    private String number;
}
