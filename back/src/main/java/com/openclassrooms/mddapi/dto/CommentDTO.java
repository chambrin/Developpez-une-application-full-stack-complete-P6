package com.openclassrooms.mddapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {
    private Long id;
    private Long articleId;
    private String username;
    private String content;
    private Date createdAt;
    private Date updatedAt;
}
