package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.dto.CreateCommentDTO;

import java.util.List;

public interface IFeedbackManager {
    CommentDTO createComment(Long authorId, CreateCommentDTO feedbackData);
    List<CommentDTO> getCommentsByArticleId(Long postId);
}