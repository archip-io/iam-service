package com.archipio.iamservice.service.impl;

import com.archipio.iamservice.config.JwtProperties;
import com.archipio.iamservice.dto.CredentialsDto;
import com.archipio.iamservice.dto.JwtTokensDto;
import com.archipio.iamservice.exception.InvalidOrExpiredJwtTokenException;
import com.archipio.iamservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  private final JwtProperties jwtProperties;

  @Override
  public JwtTokensDto createTokens(CredentialsDto credentialsDto) {
    var accessTokenClaims =
        Map.of(
            "username", credentialsDto.getUsername(),
            "email", credentialsDto.getEmail(),
            "authorities", credentialsDto.getAuthorities());
    var refreshTokenClaims =
        Map.of(
            "username", credentialsDto.getUsername(),
            "email", credentialsDto.getEmail());
    return JwtTokensDto.builder()
        .accessToken(
            createToken(
                credentialsDto.getUsername(),
                accessTokenClaims,
                jwtProperties.getAccessToken().getTtl()))
        .refreshToken(
            createToken(
                credentialsDto.getUsername(),
                refreshTokenClaims,
                jwtProperties.getRefreshToken().getTtl()))
        .build();
  }

  @Override
  public String extractUsername(String token) {
    return extractClaim(token, claims -> claims.get("username", String.class));
  }

  @Override
  public String extractEmail(String token) {
    return extractClaim(token, claims -> claims.get("email", String.class));
  }

  @Override
  public boolean validate(String token) {
    try {
      var expiration = extractExpiration(token);
      return expiration.after(new Date());
    } catch (InvalidOrExpiredJwtTokenException e) {
      return false;
    }
  }

  private String createToken(String subject, Map<String, ?> claims, long ttl_s) {
    return Jwts.builder()
        .setSubject(subject)
        .setClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + ttl_s * 1000))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key getSigningKey() {
    byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    try {
      var claims =
          Jwts.parserBuilder()
              .setSigningKey(getSigningKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
      return claimsResolver.apply(claims);
    } catch (JwtException e) {
      throw new InvalidOrExpiredJwtTokenException();
    }
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }
}
