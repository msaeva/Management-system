package com.example.management_system.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private String comment;
    private String authorUsername;
    private String fullName;
    private String createdDate;
    private Long taskID;

    public CommentDTO(String comment, String authorUsername, String createdDate, Long taskID) {
        this.comment = comment;
        this.authorUsername = authorUsername;
        this.createdDate = createdDate;
        this.taskID = taskID;
    }

    public CommentDTO(String comment, String authorUsername, String createdDate) {
        this.comment = comment;
        this.authorUsername = authorUsername;
        this.createdDate = createdDate;
    }
}
