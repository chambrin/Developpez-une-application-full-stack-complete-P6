package com.openclassrooms.mddapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

/**
 * Service de gestion des tokens JWT (JSON Web Tokens).
 * Responsable de la génération et du décodage des tokens d'authentification.
 * Respecte le principe de responsabilité unique (SRP) en se concentrant uniquement sur les opérations JWT.
 */
@Service
public class JWTService {

  // Configuration externalisée via application.properties (Open/Closed Principle)
  @Value("${mdd.app.jwtsecret}")
  private String jwtSecret;

  @Value("${mdd.app.expiretime}")
  private int jwtExpirationTime;

  /**
   * Génère la clé de signature HMAC à partir du secret configuré.
   * Méthode privée pour encapsuler la logique de création de clé.
   * @return Clé de signature pour les tokens JWT
   */
  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Génère un token JWT pour un utilisateur donné.
   * Le token contient l'email comme subject et a une durée de vie configurée.
   * @param email Email de l'utilisateur pour lequel générer le token
   * @return Token JWT signé et encodé
   */
  public String generateToken(String email) {
    Instant now = Instant.now();
    return Jwts
      .builder()
      .setSubject(email) // L'email sert d'identifiant unique
      .setIssuedAt(new Date(System.currentTimeMillis())) // Date de création
      .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime)) // Date d'expiration
      .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signature HMAC-SHA256
      .compact(); // Génération du token final
  }

  /**
   * Décode et valide un token JWT.
   * Vérifie la signature et extrait les claims du token.
   * @param jwt Token JWT à décoder
   * @return Claims contenus dans le token
   * @throws io.jsonwebtoken.JwtException si le token est invalide ou expiré
   */
  public Claims decodeToken(String jwt) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey()) // Utilise la même clé pour vérifier la signature
        .build()
        .parseClaimsJws(jwt) // Parse et valide le token
        .getBody(); // Retourne les claims
  }
}
