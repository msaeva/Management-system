package com.example.management_system.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PrivateSimpleUserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String role;
}
