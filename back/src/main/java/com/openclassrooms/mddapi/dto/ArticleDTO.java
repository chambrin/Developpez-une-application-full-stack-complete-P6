package com.openclassrooms.mddapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleDTO {
    private Long id;
    private String author;
    private String topic;
    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;
}
