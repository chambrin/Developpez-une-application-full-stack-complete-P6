package com.openclassrooms.mddapi.dto;

import lombok.Data;

import java.util.Date;

/**
 * DTO DE SORTIE - Données renvoyées au CLIENT
 * 
 * Responsabilités :
 * - Formatage des données pour le CLIENT
 * - Masquage des informations sensibles
 * - Optimisation des données transférées
 * 
 * Flux : DATABASE ──▶ ENTITY ──▶ SERVICE ──▶ ArticleDTO ──▶ CONTROLLER ──▶ CLIENT (JSON)
 * 
 * Transformation ENTITY → DTO :
 * - Article.author.username → ArticleDTO.author (String)
 * - Article.topic.title → ArticleDTO.topic (String)
 * - Autres champs : copie directe
 */
@Data
public class ArticleDTO {
    /** ID unique de l'article (généré par la DATABASE) */
    private Long id;
    
    /** Nom d'utilisateur de l'auteur (transformé depuis User.username) */
    private String author;
    
    /** Titre du sujet (transformé depuis Topic.title) */
    private String topic;
    
    /** Titre de l'article */
    private String title;
    
    /** Contenu de l'article */
    private String content;
    
    /** Date de création (générée automatiquement) */
    private Date createdAt;
    
    /** Date de dernière modification */
    private Date updatedAt;
}
