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

    public DetailedUserDTO(Long id, String username, String firstName, String lastName, String email, String role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }
}
