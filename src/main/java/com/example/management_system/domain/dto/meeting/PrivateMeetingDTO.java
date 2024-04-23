package com.example.management_system.domain.dto.meeting;

import com.example.management_system.domain.dto.team.DetailedTeamDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PrivateMeetingDTO {
    private Long id;
    private String title;
    private Long start;
    private Long end;
    private List<DetailedTeamDTO> teams;
}
