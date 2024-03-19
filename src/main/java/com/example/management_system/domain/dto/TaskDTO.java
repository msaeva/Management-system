package com.example.management_system.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
    public String title;
    public String description;
    public String status;
    public Long projectId;
    public Long userId;
}
