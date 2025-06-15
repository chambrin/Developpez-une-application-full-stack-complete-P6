package com.openclassrooms.mddapi.dto;

import lombok.Data;

/**
 * DTO D'ENTRÉE - Données reçues du CLIENT
 * 
 * Responsabilités :
 * - Réception des données JSON du CLIENT
 * - Validation des données d'entrée
 * - Transfert sécurisé des données vers le SERVICE
 * 
 * Flux : CLIENT (JSON) ──▶ CONTROLLER ──▶ CreateArticleDTO ──▶ SERVICE
 * 
 * Avantages :
 * - Sécurité : Contrôle strict des données acceptées
 * - Découplage : Indépendant du modèle de base de données
 * - Validation : Point central pour les règles métier
 */
@Data
public class CreateArticleDTO {
    /** ID du sujet/topic auquel appartient l'article */
    private Long topicId;
    
    /** Titre de l'article (obligatoire) */
    private String title;
    
    /** Contenu de l'article (obligatoire) */
    private String content;
    
    // Note : Pas d'ID, createdAt, updatedAt car générés automatiquement
    // Note : Pas d'author car extrait du token JWT
}
