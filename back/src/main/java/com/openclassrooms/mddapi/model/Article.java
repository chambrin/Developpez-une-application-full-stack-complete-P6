package com.openclassrooms.mddapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * ENTITY ARTICLE - Modèle de données persistant
 * 
 * Responsabilités :
 * - Représentation de la structure de données en DATABASE
 * - Mapping objet-relationnel (ORM) avec JPA
 * - Gestion automatique des timestamps
 * - Relations avec les autres entités
 * 
 * Flux : SERVICE ──▶ ENTITY ──▶ DATABASE (via Repository/JPA)
 * 
 * Mapping DATABASE :
 * - Table : articles
 * - Relations : ManyToOne vers User et Topic
 * - Contraintes : NOT NULL sur les champs obligatoires
 */
@Entity
@Table(name = "articles") // Nom de la table en DATABASE
@Data // Lombok : génération automatique des getters/setters
public class Article {

    /** Clé primaire auto-générée par la DATABASE */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;

    /** 
     * Relation ManyToOne vers User (auteur)
     * Clé étrangère : author_id dans la table articles
     * Un utilisateur peut avoir plusieurs articles
     */
	@ManyToOne
	@JoinColumn(name = "author_id", nullable = false)
	private User author;

    /**
     * Relation ManyToOne vers Topic (sujet)
     * Clé étrangère : topic_id dans la table articles
     * Un topic peut avoir plusieurs articles
     */
	@ManyToOne
	@JoinColumn(name = "topic_id", nullable = false)
	private Topic topic;

    /** Titre de l'article (obligatoire) */
	@Column(nullable = false)
	private String title;

    /** Contenu de l'article (TEXT pour contenu long) */
	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

    /** Date de création (générée automatiquement, non modifiable) */
	@Column(name = "created_at", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

    /** Date de dernière modification (mise à jour automatique) */
	@Column(name = "updated_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

    /**
     * CALLBACK JPA - Exécuté avant la persistance en DATABASE
     * Initialise automatiquement les timestamps
     */
	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

    /**
     * CALLBACK JPA - Exécuté avant chaque mise à jour en DATABASE
     * Met à jour automatiquement le timestamp de modification
     */
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}
}
