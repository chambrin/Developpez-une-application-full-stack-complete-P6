package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PostDataAccess extends JpaRepository<Article, Long> {
    
    @Query("SELECT p FROM Article p WHERE p.topic IN :subjectList ORDER BY p.createdAt DESC")
    List<Article> findPostsBySubjectsOrderedByDate(@Param("subjectList") Set<Topic> subjectList);
}