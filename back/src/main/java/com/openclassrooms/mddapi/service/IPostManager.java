package com.openclassrooms.mddapi.service;

import java.util.List;
import java.util.Optional;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.CreateArticleDTO;

public interface IPostManager {
    List<ArticleDTO> getArticlesForUser(Long accountId);
    Optional<ArticleDTO> getArticle(Long postId);
    ArticleDTO createArticle(Long authorId, CreateArticleDTO postCreationData);
}