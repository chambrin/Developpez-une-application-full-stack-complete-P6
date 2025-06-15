package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.openclassrooms.mddapi.service.ISubjectManager;
import com.openclassrooms.mddapi.dto.TopicDTO;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des sujets/topics.
 * Permet aux utilisateurs de consulter, s'abonner et se désabonner des sujets.
 * Respecte le principe de responsabilité unique (SRP) en se concentrant uniquement sur les opérations liées aux sujets.
 */
@RestController
@RequestMapping("/api/topics")
public class SubjectController {
    // Injection de dépendance via le constructeur (Dependency Inversion Principle)
    private ISubjectManager subjectService;

    /**
     * Constructeur avec injection de dépendance.
     * @param subjectService Service de gestion des sujets
     */
    public SubjectController(ISubjectManager subjectService) {
        this.subjectService = subjectService;
    }

    /**
     * Récupère tous les sujets disponibles dans l'application.
     * @return Liste de tous les sujets sous forme de TopicDTO
     */
    @Operation(summary = "Récupérer tous les sujets disponibles")
    @GetMapping("/all")
    public ResponseEntity<List<TopicDTO>> retrieveAllSubjects() {
        List<TopicDTO> allSubjects = subjectService.getAllTopics();
        return ResponseEntity.ok(allSubjects);
    }

    /**
     * Récupère les sujets auxquels l'utilisateur authentifié n'est pas encore abonné.
     * @return Liste des sujets disponibles pour abonnement
     */
    @Operation(summary = "Récupérer les sujets non souscrits par l'utilisateur")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/not-subscribed")
    public ResponseEntity<List<TopicDTO>> retrieveAvailableSubjects() {
        User authenticatedUser = extractAuthenticatedUser();
        List<TopicDTO> availableSubjects = subjectService.getUnsubscribedTopics(authenticatedUser.getId());
        return ResponseEntity.ok(availableSubjects);
    }

    /**
     * Récupère les sujets auxquels l'utilisateur authentifié est abonné.
     * @return Liste des sujets suivis par l'utilisateur
     */
    @Operation(summary = "Récupérer les sujets souscrits par l'utilisateur")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/subscribed")
    public ResponseEntity<List<TopicDTO>> retrieveFollowedSubjects() {
        User authenticatedUser = extractAuthenticatedUser();
        List<TopicDTO> followedSubjects = subjectService.getSubscribedTopics(authenticatedUser.getId());
        return ResponseEntity.ok(followedSubjects);
    }

    /**
     * Permet à l'utilisateur authentifié de s'abonner à un sujet.
     * @param subjectId Identifiant du sujet à suivre
     * @return Réponse HTTP 200 si l'abonnement est réussi
     */
    @Operation(summary = "S'abonner à un sujet")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/subscribe/{subjectId}")
    public ResponseEntity<Void> followSubject(@PathVariable Long subjectId) {
        User authenticatedUser = extractAuthenticatedUser();
        subjectService.subscribeToTopic(authenticatedUser.getId(), subjectId);
        return ResponseEntity.ok().build();
    }

    /**
     * Permet à l'utilisateur authentifié de se désabonner d'un sujet.
     * @param subjectId Identifiant du sujet à ne plus suivre
     * @return Réponse HTTP 200 si le désabonnement est réussi
     */
    @Operation(summary = "Se désabonner d'un sujet")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/unsubscribe/{subjectId}")
    public ResponseEntity<Void> unfollowSubject(@PathVariable Long subjectId) {
        User authenticatedUser = extractAuthenticatedUser();
        subjectService.unsubscribeFromTopic(authenticatedUser.getId(), subjectId);
        return ResponseEntity.ok().build();
    }

    /**
     * Méthode utilitaire pour extraire l'utilisateur authentifié du contexte de sécurité.
     * Centralise la logique d'extraction de l'utilisateur (DRY principle).
     * @return L'utilisateur actuellement authentifié
     * @throws RuntimeException si l'authentification échoue ou si le principal n'est pas valide
     */
    private User extractAuthenticatedUser() {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth == null || !(currentAuth.getPrincipal() instanceof User)) {
            throw new RuntimeException("Authentication verification failed or invalid user principal");
        }
        return (User) currentAuth.getPrincipal();
    }
}