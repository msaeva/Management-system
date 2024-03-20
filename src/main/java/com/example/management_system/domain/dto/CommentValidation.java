package com.example.management_system.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentValidation {
    private String comment;
    private Long authorID;
    private Long taskID;
}
