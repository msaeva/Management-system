package com.example.management_system.domain.dto.user;

import com.example.management_system.domain.dto.project.DetailedProjectDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DetailedUserDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private List<DetailedProjectDTO> projects;
}
