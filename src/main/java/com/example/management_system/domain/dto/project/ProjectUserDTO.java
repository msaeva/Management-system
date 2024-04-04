package com.example.management_system.domain.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectUserDTO {
    private Long id;
    private String title;
    private List<Long> userIds;
}
