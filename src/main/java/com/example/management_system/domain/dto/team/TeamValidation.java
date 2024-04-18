package com.example.management_system.domain.dto.team;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamValidation {

    private String name;
    private List<Long> userIds;

    @NotNull
    private Long projectId;
}
