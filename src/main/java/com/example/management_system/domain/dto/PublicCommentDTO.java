package com.example.management_system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PublicCommentDTO {
    private Long id;
    private String comment;
    private String fullName;
    private Long authorId;
    private LocalDateTime createdDate;
    private Long taskId;
}
