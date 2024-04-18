package com.example.management_system.domain.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordValidation {
    @Size(min = 8, max = 25)
    private String oldPassword;
    @Size(min = 8, max = 25)
    private String newPassword;
}
