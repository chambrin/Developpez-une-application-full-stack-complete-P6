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

@Service
public class JWTService {

  @Value("${mdd.app.jwtsecret}")
  private String jwtSecret;

  @Value("${mdd.app.expiretime}")
  private int jwtExpirationTime;

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(String email) {
    Instant now = Instant.now();
    return Jwts
      .builder()
      .setSubject(email)
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
      .signWith(getSigningKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public Claims decodeToken(String jwt) {
    return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwt).getBody();
  }
}
