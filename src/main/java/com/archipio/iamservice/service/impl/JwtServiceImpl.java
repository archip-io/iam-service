package com.archipio.iamservice.service.impl;

import com.archipio.iamservice.config.JwtProperties;
import com.archipio.iamservice.dto.CredentialsDto;
import com.archipio.iamservice.dto.JwtTokensDto;
import com.archipio.iamservice.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
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

  private String createToken(String subject, Map<String, ?> claims, long ttl_ms) {
    return Jwts.builder()
        .setSubject(subject)
        .setClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + ttl_ms))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key getSigningKey() {
    byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
