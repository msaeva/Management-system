package com.example.management_system.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectValidation {
    public String title;
    public String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
