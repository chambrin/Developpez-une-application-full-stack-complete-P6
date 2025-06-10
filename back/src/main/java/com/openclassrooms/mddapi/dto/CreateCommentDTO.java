package com.openclassrooms.mddapi.dto;

import lombok.Data;

@Data
public class CreateCommentDTO {
    private Long articleId;
    private String content;
}
