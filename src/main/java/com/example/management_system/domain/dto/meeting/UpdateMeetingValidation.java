package com.example.management_system.domain.dto.meeting;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMeetingValidation {
    @NotNull
    @Size(min = 3)
    private String title;

    @NotNull
    private long start;

    @NotNull
    private long end;

}
