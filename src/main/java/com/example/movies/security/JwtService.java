package com.example.movies.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static com.example.movies.constant.Constant.CLAIM_EMAIL;
import static com.example.movies.constant.Constant.CLAIM_ROLE;

@Service
public class JwtService {

  private final SecretKey key;
  private final long validityMillis;

  public JwtService(@Value("${security.jwt.secret}") String secret,
                    @Value("${security.jwt.access-token-validity-minutes}") long validityMinutes) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.validityMillis = validityMinutes * 60_000L;
  }

  public String generateAccessToken(AuthenticatedUser user) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(String.valueOf(user.getId()))
        .claim(CLAIM_EMAIL, user.getEmail())
        .claim(CLAIM_ROLE, user.getRole().name())
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusMillis(validityMillis)))
        .signWith(key)
        .compact();
  }

  public Claims parseAccessToken(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}