package com.example.management_system.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserValidation {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String role;
}
