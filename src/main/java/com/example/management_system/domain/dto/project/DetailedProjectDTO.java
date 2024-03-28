package com.example.management_system.domain.dto.project;

import com.example.management_system.domain.dto.team.DetailedTeamDTO;
import com.example.management_system.domain.dto.user.SimpleUserDTO;
import com.example.management_system.domain.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DetailedProjectDTO {
    private Long id;
    private String title;
    private String description;
    private String abbreviation;
    private ProjectStatus status;
    private LocalDateTime createdDate;
    private List<DetailedTeamDTO> teams;
    private List<SimpleUserDTO> pms;
}
