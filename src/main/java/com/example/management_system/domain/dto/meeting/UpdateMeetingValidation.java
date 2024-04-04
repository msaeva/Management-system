package com.example.management_system.domain.dto.meeting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMeetingValidation {
    private String title;
    private long start;
    private long end;

}
