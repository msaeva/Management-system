package com.example.management_system.domain.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserValidation {
    @NotNull
    @Size(min = 3)
    private String firstName;
    @NotNull
    @Size(min = 3)
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 3)
    private String username;

    @NotNull
    @Size(min = 6)
    private String password;
    private String role;
}
