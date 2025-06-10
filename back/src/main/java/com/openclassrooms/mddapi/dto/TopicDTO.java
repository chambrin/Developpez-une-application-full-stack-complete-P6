package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class TopicDTO {
    private Long id;
    private String title;
    private String description;

    public TopicDTO(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}