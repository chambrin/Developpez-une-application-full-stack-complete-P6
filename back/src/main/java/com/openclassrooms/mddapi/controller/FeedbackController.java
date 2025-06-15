package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.openclassrooms.mddapi.service.IFeedbackManager;
import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.dto.CreateCommentDTO;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class FeedbackController {
    private IFeedbackManager feedbackService;

    public FeedbackController(IFeedbackManager feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Operation(summary = "Récupérer tous les commentaires d'un article")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{articleId}")
    public ResponseEntity<List<CommentDTO>> retrieveArticleFeedbacks(@PathVariable Long articleId) {
        List<CommentDTO> articleFeedbacks = feedbackService.getCommentsByArticleId(articleId);
        return ResponseEntity.ok(articleFeedbacks);
    }

    @Operation(summary = "Créer un nouveau commentaire")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<CommentDTO> submitNewFeedback(@RequestBody CreateCommentDTO feedbackData) {
        User authenticatedUser = extractAuthenticatedUser();
        CommentDTO submittedFeedback = feedbackService.createComment(authenticatedUser.getId(), feedbackData);
        return ResponseEntity.ok(submittedFeedback);
    }

    private User extractAuthenticatedUser() {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth == null || !(currentAuth.getPrincipal() instanceof User)) {
            throw new RuntimeException("Authentication verification failed or invalid user principal");
        }
        return (User) currentAuth.getPrincipal();
    }
}