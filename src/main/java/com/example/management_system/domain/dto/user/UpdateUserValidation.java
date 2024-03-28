package com.example.management_system.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserValidation {
    private Long id;
    private String firstName;
    private String lastName;
    private String role;

    @Override
    public String toString() {
        return "UpdateUserValidation{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
