package com.example.management_system.domain.dto.meeting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PublicMeetingDTO {
    private Long id;
    private String title;
    private Long start;
    private Long end;
}
