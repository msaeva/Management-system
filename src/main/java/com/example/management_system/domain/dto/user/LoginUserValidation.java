package com.example.management_system.domain.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUserValidation {
    @NotNull
    @Size(min = 4)
    public String username;

    @NotNull
    @Size(min = 6)
    public String password;
}
