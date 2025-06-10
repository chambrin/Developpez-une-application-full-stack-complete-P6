package com.openclassrooms.mddapi.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.openclassrooms.mddapi.repository.FeedbackDataAccess;
import com.openclassrooms.mddapi.repository.AccountDataAccess;
import com.openclassrooms.mddapi.repository.PostDataAccess;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.dto.CreateCommentDTO;

@Service
public class FeedbackManager implements IFeedbackManager {
    private final FeedbackDataAccess responseRepository;
    private final AccountDataAccess userRepository;
    private final PostDataAccess contentRepository;

    public FeedbackManager(FeedbackDataAccess responseRepository, AccountDataAccess userRepository, PostDataAccess contentRepository) {
        this.responseRepository = responseRepository;
        this.userRepository = userRepository;
        this.contentRepository = contentRepository;
    }

    @Override
    public CommentDTO createComment(Long authorId, CreateCommentDTO feedbackData) {
        User writer = userRepository.findAccountById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));
        Article targetPost = contentRepository.findById(feedbackData.getArticleId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
    
        Comment newResponse = new Comment();
        newResponse.setAuthor(writer);
        newResponse.setArticle(targetPost);
        newResponse.setContent(feedbackData.getContent());
    
        Comment persistedResponse = responseRepository.save(newResponse);
        return transformToDto(persistedResponse);
    }
    
    @Override
    public List<CommentDTO> getCommentsByArticleId(Long postId) {
        List<Comment> responseList = responseRepository.findByArticleIdOrderByCreatedAtAsc(postId);
        return responseList.stream()
                .map(this::transformToDto)
                .collect(Collectors.toList());
    }

    private CommentDTO transformToDto(Comment response) {
        CommentDTO responseDto = new CommentDTO();
        responseDto.setId(response.getId());
        responseDto.setArticleId(response.getArticle().getId());
        responseDto.setUsername(response.getAuthor().getUsername());
        responseDto.setContent(response.getContent());
        responseDto.setCreatedAt(response.getCreatedAt());
        responseDto.setUpdatedAt(response.getUpdatedAt());
        return responseDto;
    }
}