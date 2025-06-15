package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.openclassrooms.mddapi.service.IPostManager;
import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.CreateArticleDTO;

import java.util.List;
import java.util.Optional;

/**
 * COUCHE CONTROLLER - Point d'entrée de l'API REST
 * 
 * Responsabilités :
 * - Réception des requêtes HTTP du CLIENT
 * - Validation de l'authentification JWT
 * - Transformation des données HTTP en DTOs
 * - Délégation du traitement métier au SERVICE
 * - Retour de la réponse HTTP au CLIENT
 * 
 * Flux de données : CLIENT ──▶ CONTROLLER ──▶ DTO ──▶ SERVICE
 */
@RestController
@RequestMapping("/api/articles")
public class PostController {
    private IPostManager postService;

    /**
     * Injection de dépendance du service (Principe SOLID - Dependency Inversion)
     */
    public PostController(IPostManager postService) {
        this.postService = postService;
    }

    @Operation(summary = "Récupérer tous les articles de l'utilisateur connecté")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseEntity<List<ArticleDTO>> retrieveUserPosts() {
        User authenticatedUser = extractCurrentUser();
        List<ArticleDTO> userPosts = postService.getArticlesForUser(authenticatedUser.getId());
        return ResponseEntity.ok(userPosts);
    }

    @Operation(summary = "Récupérer un article par son ID")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{postId}")
    public ResponseEntity<Optional<ArticleDTO>> retrieveSinglePost(@PathVariable Long postId) {
        User authenticatedUser = extractCurrentUser();
        Optional<ArticleDTO> requestedPost = postService.getArticle(postId);
        return ResponseEntity.ok(requestedPost);
    }

    /**
     * ENDPOINT DE CRÉATION D'UN POST
     * 
     * FLUX COMPLET : CLIENT ──▶ CONTROLLER ──▶ DTO ──▶ SERVICE ──▶ ENTITY ──▶ DATABASE
     * 
     * Étapes :
     * 1. CLIENT : Envoi d'une requête POST avec les données JSON
     * 2. CONTROLLER : Réception et validation de l'authentification
     * 3. DTO : Désérialisation JSON vers CreateArticleDTO (données d'entrée)
     * 4. SERVICE : Traitement métier et transformation DTO → ENTITY
     * 5. ENTITY : Persistance en base de données
     * 6. DATABASE : Stockage et génération de l'ID
     * 7. Retour : ENTITY → DTO → JSON → CLIENT
     */
    @Operation(summary = "Créer un nouvel article")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ResponseEntity<ArticleDTO> publishNewPost(@RequestBody CreateArticleDTO newPostData) {
        // ÉTAPE 1 : Extraction de l'utilisateur authentifié via JWT
        User authenticatedUser = extractCurrentUser();
        
        // ÉTAPE 2 : Délégation au SERVICE avec DTO d'entrée
        // Le SERVICE va transformer le DTO en ENTITY et persister en DATABASE
        ArticleDTO publishedPost = postService.createArticle(authenticatedUser.getId(), newPostData);
        
        // ÉTAPE 3 : Retour du DTO de sortie au CLIENT
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