package com.openclassrooms.mddapi.model;

import jakarta.persistence.*;
import lombok.*;

import net.minidev.json.annotate.JsonIgnore;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "topics")
@Getter
@Setter
@EqualsAndHashCode(exclude = "subscribers", of = "id")
@ToString(exclude = "subscribers")
public class Topic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column
	private String description;

	@Column(name = "created_at", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "updated_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}

	@ManyToMany(mappedBy = "subscribedTopics")
	@JsonIgnore
	private Set<User> subscribers = new HashSet<>();
	
}
