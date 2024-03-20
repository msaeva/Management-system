package com.example.management_system.service.mapper;

import com.example.management_system.domain.dto.project.ProjectValidation;
import com.example.management_system.domain.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "jsr330")
public abstract class ProjectMapper {
    @Mappings({
            @Mapping(target = "createdDate", expression = "java(java.time.LocalDateTime.now())"),
            @Mapping(target = "status", constant = "STARTED")
    })
    public abstract Project toProject(ProjectValidation validation);
}
