package com.example.management_system.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String comment;
    private String authorUsername;
    private String fullName;
    private Long authorId;
    private String createdDate;
    private Long taskID;

    public CommentDTO(Long id, String comment, String fullName, Long authorId, String createdDate, Long taskID) {
        this.id = id;
        this.comment = comment;
        this.fullName = fullName;
        this.authorId = authorId;
        this.createdDate = createdDate;
        this.taskID = taskID;
    }

    public CommentDTO(Long id, String comment, String fullName, Long authorId, String createdDate) {
        this.id = id;
        this.comment = comment;
        this.fullName = fullName;
        this.authorId = authorId;
        this.createdDate = createdDate;
    }
}
