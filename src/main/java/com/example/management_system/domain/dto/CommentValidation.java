package com.example.management_system.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentValidation {
    @NotNull
    private String comment;
    @NotNull
    private Long authorID;
    @NotNull
    private Long taskID;
}
