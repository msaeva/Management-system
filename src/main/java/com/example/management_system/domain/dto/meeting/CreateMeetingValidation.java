package com.example.management_system.domain.dto.meeting;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateMeetingValidation {
    private Long projectId;
    private List<Long> teamIds;
    private Long start;
    private Long end;
    private String title;
}
