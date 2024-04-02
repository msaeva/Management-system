package com.example.management_system.domain.dto;

import com.example.management_system.domain.dto.project.PrivateProjectDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectTaskDTO {
    private PrivateProjectDTO project;
    private List<SimpleTaskDTO> tasks;
}
