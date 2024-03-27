package com.archipio.iamservice.unittest.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

import com.archipio.iamservice.config.JwtProperties;
import com.archipio.iamservice.dto.CredentialsDto;
import com.archipio.iamservice.exception.InvalidOrExpiredJwtTokenException;
import com.archipio.iamservice.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
class JwtServiceImplTest {

  @Mock private JwtProperties jwtProperties;

  @InjectMocks private JwtServiceImpl jwtService;

  @Test
  public void createTokens_whenCredentialsIsValid_thenReturnJwtTokens() {
    // Prepare
    final var username = "user";
    final var email = "user@mail.ru";
    final var authorities = List.of("read", "write");
    final var secret = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327854";
    final var accessTokenTtl = 5 * 60 * 1000L;
    final var refreshTokenTtl = 7 * 24 * 60 * 60 * 1000L;
    CredentialsDto credentialsDto =
        CredentialsDto.builder().username(username).email(email).authorities(authorities).build();
    when(jwtProperties.getSecret()).thenReturn(secret);
    when(jwtProperties.getAccessToken())
        .thenReturn(JwtProperties.AccessTokenProperties.builder().ttl(accessTokenTtl).build());
    when(jwtProperties.getRefreshToken())
        .thenReturn(JwtProperties.RefreshTokenProperties.builder().ttl(refreshTokenTtl).build());

    // Do
    var jwtTokensDto = jwtService.createTokens(credentialsDto);

    // Check
    Claims claims =
        Jwts.parserBuilder()
            .setSigningKey(getSigningKey(secret))
            .build()
            .parseClaimsJws(jwtTokensDto.getAccessToken())
            .getBody();
    assertThat(claims.get("username", String.class)).isEqualTo(username);
    assertThat(claims.get("email", String.class)).isEqualTo(email);
    assertThat(claims.get("authorities", List.class))
        .containsExactlyInAnyOrderElementsOf(authorities);
  }

  @Test
  public void extractUsername_whenTokenIsValid_thenReturnUsername() {
    // Prepare
    final var username = "user";
    final var secret = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327854";
    final var tokenTtl = 7 * 24 * 60 * 60 * 1000L;
    final var token =
        createToken(
            secret,
            username,
            Map.of("username", username),
            new Date(System.currentTimeMillis() + tokenTtl));
    when(jwtProperties.getSecret()).thenReturn(secret);

    // Do
    var actualUsername = jwtService.extractUsername(token);

    // Check
    assertThat(actualUsername).isEqualTo(username);
  }

  @Test
  public void
      extractUsername_whenTokenHasInvalidSign_thenThrownInvalidOrExpiredJwtTokenException() {
    // Prepare
    final var username = "user";
    final var secret1 = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327853";
    final var secret2 = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327854";
    final var tokenTtl = 7 * 24 * 60 * 60 * 1000L;
    final var token =
        createToken(
            secret1,
            username,
            Map.of("username", username),
            new Date(System.currentTimeMillis() + tokenTtl));
    when(jwtProperties.getSecret()).thenReturn(secret2);

    // Do and Check
    assertThatExceptionOfType(InvalidOrExpiredJwtTokenException.class)
        .isThrownBy(() -> jwtService.extractUsername(token));
  }

  @Test
  public void extractEmail_whenTokenIsValid_thenReturnEmail() {
    // Prepare
    final var email = "email";
    final var secret = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327854";
    final var tokenTtl = 7 * 24 * 60 * 60 * 1000L;
    final var token =
        createToken(
            secret, email, Map.of("email", email), new Date(System.currentTimeMillis() + tokenTtl));
    when(jwtProperties.getSecret()).thenReturn(secret);

    // Do
    var actualEmail = jwtService.extractEmail(token);

    // Check
    assertThat(actualEmail).isEqualTo(email);
  }

  @Test
  public void extractEmail_whenTokenHasInvalidSign_thenThrownInvalidOrExpiredJwtTokenException() {
    // Prepare
    final var email = "email";
    final var secret1 = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327853";
    final var secret2 = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327854";
    final var tokenTtl = 7 * 24 * 60 * 60 * 1000L;
    final var token =
        createToken(
            secret1,
            email,
            Map.of("email", email),
            new Date(System.currentTimeMillis() + tokenTtl));
    when(jwtProperties.getSecret()).thenReturn(secret2);

    // Do and Check
    assertThatExceptionOfType(InvalidOrExpiredJwtTokenException.class)
        .isThrownBy(() -> jwtService.extractEmail(token));
  }

  @Test
  public void validate_whenTokenIsValid_thenReturnTrue() {
    // Prepare
    final var username = "user";
    final var secret = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327854";
    final var tokenTtl = 7 * 24 * 60 * 60 * 1000L;
    final var expiration = new Date(System.currentTimeMillis() + tokenTtl);
    final var token = createToken(secret, username, Map.of("username", username), expiration);
    when(jwtProperties.getSecret()).thenReturn(secret);

    // Do
    var isValid = jwtService.validate(token);

    // Check
    assertThat(isValid).isTrue();
  }

  @Test
  public void validate_whenTokenHasExpired_thenReturnFalse() {
    // Prepare
    final var username = "user";
    final var secret = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327854";
    final var tokenTtl = 7 * 24 * 60 * 60 * 1000L;
    final var expiration = new Date(System.currentTimeMillis() - tokenTtl);
    final var token = createToken(secret, username, Map.of("username", username), expiration);
    when(jwtProperties.getSecret()).thenReturn(secret);

    // Do
    var isValid = jwtService.validate(token);

    // Check
    assertThat(isValid).isFalse();
  }

  @Test
  public void validate_whenTokenHasInvalidSign_thenReturnFalse() {
    // Prepare
    final var username = "user";
    final var secret1 = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327853";
    final var secret2 = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327854";
    final var tokenTtl = 7 * 24 * 60 * 60 * 1000L;
    final var expiration = new Date(System.currentTimeMillis() + tokenTtl);
    final var token = createToken(secret1, username, Map.of("username", username), expiration);
    when(jwtProperties.getSecret()).thenReturn(secret2);

    // Do
    var isValid = jwtService.validate(token);

    // Check
    assertThat(isValid).isFalse();
  }

  private Key getSigningKey(String secret) {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private String createToken(
      String secret, String subject, Map<String, ?> claims, Date expiration) {
    return Jwts.builder()
        .setSubject(subject)
        .setClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(expiration)
        .signWith(getSigningKey(secret), SignatureAlgorithm.HS256)
        .compact();
  }
}
