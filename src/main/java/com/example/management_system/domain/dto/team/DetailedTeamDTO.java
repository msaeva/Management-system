package com.example.management_system.domain.dto.team;

import com.example.management_system.domain.dto.user.SimpleUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DetailedTeamDTO {
    private Long id;
    private String name;
    private List<SimpleUserDTO> users;
}
