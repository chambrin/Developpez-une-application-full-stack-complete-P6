package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class CreateArticleDTO {
    private Long topicId;
    private String title;
    private String content;
}
