package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectDataAccess extends JpaRepository<Topic, Long> {
    // Méthodes héritées de JpaRepository suffisantes pour les opérations de base
}