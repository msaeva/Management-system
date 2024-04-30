package com.example.management_system.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserValidation {
    @NotBlank
    private Long id;

    @NotBlank(message = "Name may not be blank")
    @Size(min = 3, max = 15, message = "Size must be between 3 and 15 chars long")
    private String firstName;

    @NotBlank(message = "Name may not be blank")
    @Size(min = 3, max = 15, message = "Size must be between 3 and 15 chars long")
    private String lastName;

    @NotBlank
    private String role;
}
