package com.example.management_system.domain.dto.meeting;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateMeetingValidation {
    @NotNull
    private Long projectId;

    @NotNull
    private List<Long> teamIds;

    @NotNull
    private Long start;

    @NotNull
    private Long end;

    @NotNull
    @Size(min = 3)
    private String title;
}
