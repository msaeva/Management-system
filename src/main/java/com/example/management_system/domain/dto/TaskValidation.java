package com.example.management_system.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskValidation {
    public String title;
    public String description;
    public Long projectId;
    public Long userId;
}
