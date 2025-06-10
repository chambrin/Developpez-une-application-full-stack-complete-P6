package com.openclassrooms.mddapi.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.openclassrooms.mddapi.repository.PostDataAccess;
import com.openclassrooms.mddapi.repository.AccountDataAccess;
import com.openclassrooms.mddapi.repository.SubjectDataAccess;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.CreateArticleDTO;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostManager implements IPostManager {
    private final PostDataAccess contentRepository;
    private final AccountDataAccess userRepository;
    private final SubjectDataAccess categoryRepository;

    public PostManager(PostDataAccess contentRepository, AccountDataAccess userRepository, SubjectDataAccess categoryRepository) {
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public List<ArticleDTO> getArticlesForUser(Long accountId) {
        User account = userRepository.findAccountById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        List<Article> contentList = contentRepository.findPostsBySubjectsOrderedByDate(account.getSubscribedTopics());
        return contentList.stream()
                .map(this::transformToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<ArticleDTO> getArticle(Long postId) {
        Optional<Article> content = contentRepository.findById(postId);
        return content.map(this::transformToDto);
    }

    @Override
    @Transactional
    public ArticleDTO createArticle(Long authorId, CreateArticleDTO postCreationData) {
        User writer = userRepository.findAccountById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));
        Topic category = categoryRepository.findById(postCreationData.getTopicId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Article newContent = new Article();
        newContent.setAuthor(writer);
        newContent.setTopic(category);
        newContent.setTitle(postCreationData.getTitle());
        newContent.setContent(postCreationData.getContent());

        Article persistedContent = contentRepository.save(newContent);
        return transformToDto(persistedContent);
    }

    private ArticleDTO transformToDto(Article content) {
        ArticleDTO responseDto = new ArticleDTO();
        responseDto.setId(content.getId());
        responseDto.setAuthor(content.getAuthor().getUsername());
        responseDto.setTopic(content.getTopic().getTitle());
        responseDto.setTitle(content.getTitle());
        responseDto.setContent(content.getContent());
        responseDto.setCreatedAt(content.getCreatedAt());
        responseDto.setUpdatedAt(content.getUpdatedAt());
        return responseDto;
    }
}