package com.example.management_system.domain.dto.project;

import com.example.management_system.domain.dto.task.SimpleTaskDTO;
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
