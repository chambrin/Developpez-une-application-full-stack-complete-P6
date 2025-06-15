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

/**
 * COUCHE SERVICE - Logique métier et orchestration
 * 
 * Responsabilités :
 * - Traitement de la logique métier
 * - Transformation DTO ↔ ENTITY
 * - Orchestration des appels aux REPOSITORIES
 * - Gestion des transactions
 * 
 * Flux : CONTROLLER ──▶ SERVICE ──▶ REPOSITORY ──▶ DATABASE
 */
@Service
public class PostManager implements IPostManager {
    // Injection des repositories pour accès aux données
    private final PostDataAccess contentRepository;
    private final AccountDataAccess userRepository;
    private final SubjectDataAccess categoryRepository;

    /**
     * Injection de dépendances (Principe SOLID - Dependency Inversion)
     */
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

    /**
     * CRÉATION D'UN ARTICLE - Cœur du processus
     * 
     * FLUX DÉTAILLÉ : DTO ──▶ VALIDATION ──▶ ENTITY ──▶ DATABASE ──▶ DTO
     * 
     * Étapes :
     * 1. Réception du CreateArticleDTO du CONTROLLER
     * 2. Validation et récupération des entités liées (User, Topic)
     * 3. Création de l'ENTITY Article
     * 4. Persistance en DATABASE via Repository
     * 5. Transformation ENTITY → DTO pour le retour
     */
    @Override
    @Transactional // Gestion transactionnelle pour la cohérence des données
    public ArticleDTO createArticle(Long authorId, CreateArticleDTO postCreationData) {
        // ÉTAPE 1 : Validation et récupération de l'auteur depuis la DATABASE
        User writer = userRepository.findAccountById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));
        
        // ÉTAPE 2 : Validation et récupération du topic depuis la DATABASE
        Topic category = categoryRepository.findById(postCreationData.getTopicId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // ÉTAPE 3 : Transformation DTO → ENTITY
        // Création de l'entité Article avec les données validées
        Article newContent = new Article();
        newContent.setAuthor(writer);                           // Relation ManyToOne vers User
        newContent.setTopic(category);                          // Relation ManyToOne vers Topic
        newContent.setTitle(postCreationData.getTitle());       // Données du DTO
        newContent.setContent(postCreationData.getContent());   // Données du DTO
        // Note : createdAt et updatedAt sont gérés automatiquement par @PrePersist

        // ÉTAPE 4 : Persistance en DATABASE
        // Le repository JPA génère l'ID et sauvegarde l'entité
        Article persistedContent = contentRepository.save(newContent);
        
        // ÉTAPE 5 : Transformation ENTITY → DTO pour le retour au CONTROLLER
        return transformToDto(persistedContent);
    }

    /**
     * TRANSFORMATION ENTITY → DTO
     * 
     * Responsabilités :
     * - Conversion des relations complexes en données simples
     * - Formatage des données pour le CLIENT
     * - Masquage des informations sensibles
     * 
     * Transformations :
     * - Article.author (User) → ArticleDTO.author (String)
     * - Article.topic (Topic) → ArticleDTO.topic (String)
     */
    private ArticleDTO transformToDto(Article content) {
        ArticleDTO responseDto = new ArticleDTO();
        responseDto.setId(content.getId());                           // ID généré par la DATABASE
        responseDto.setAuthor(content.getAuthor().getUsername());     // User.username → String
        responseDto.setTopic(content.getTopic().getTitle());          // Topic.title → String
        responseDto.setTitle(content.getTitle());                     // Copie directe
        responseDto.setContent(content.getContent());                 // Copie directe
        responseDto.setCreatedAt(content.getCreatedAt());             // Date de création
        responseDto.setUpdatedAt(content.getUpdatedAt());             // Date de modification
        return responseDto;
    }
}