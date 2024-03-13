package com.example.management_system.service.mapper;

import com.example.management_system.domain.dto.TaskDTO;
import com.example.management_system.domain.dto.TaskValidation;
import com.example.management_system.domain.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jsr330")
public abstract class TaskMapper {

    public abstract Task toTask(TaskValidation validation);

    public abstract TaskDTO toDTO(Task task);
}
