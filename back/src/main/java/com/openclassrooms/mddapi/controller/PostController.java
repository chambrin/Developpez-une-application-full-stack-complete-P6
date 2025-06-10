package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.mddapi.service.IPostManager;
import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.CreateArticleDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class PostController {
    private IPostManager postService;

    public PostController(IPostManager postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> retrieveUserPosts() {
        User authenticatedUser = extractCurrentUser();
        List<ArticleDTO> userPosts = postService.getArticlesForUser(authenticatedUser.getId());
        return ResponseEntity.ok(userPosts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Optional<ArticleDTO>> retrieveSinglePost(@PathVariable Long postId) {
        User authenticatedUser = extractCurrentUser();
        Optional<ArticleDTO> requestedPost = postService.getArticle(postId);
        return ResponseEntity.ok(requestedPost);
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> publishNewPost(@RequestBody CreateArticleDTO newPostData) {
        User authenticatedUser = extractCurrentUser();
        ArticleDTO publishedPost = postService.createArticle(authenticatedUser.getId(), newPostData);
        return ResponseEntity.ok(publishedPost);
    }

    private User extractCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            throw new RuntimeException("Authentication failed or user principal not found");
        }
        return (User) auth.getPrincipal();
    }
}