package com.example.management_system.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimpleUserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;

}
