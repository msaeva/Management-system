package com.example.management_system.domain.dto.project;

import com.example.management_system.domain.dto.team.SimpleTeamDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectTeamDTO {
    private Long id;
    private String title;
    private List<SimpleTeamDTO> teams;
}
