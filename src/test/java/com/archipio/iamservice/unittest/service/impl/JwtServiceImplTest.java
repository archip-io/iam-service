package com.archipio.iamservice.unittest.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.archipio.iamservice.config.JwtProperties;
import com.archipio.iamservice.dto.CredentialsWithAuthoritiesDto;
import com.archipio.iamservice.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtServiceImplTest {

  @Mock private JwtProperties jwtProperties;

  @InjectMocks private JwtServiceImpl jwtService;

  @Test
  public void createTokens_credentialsWithAuthorities_validJwtTokens() {
    // Prepare
    final var username = "user";
    final var email = "user@mail.ru";
    final var authorities = List.of("read", "write");
    final var secret = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327854";
    final var accessTokenTtl = 5 * 60 * 1000L;
    final var refreshTokenTtl = 7 * 24 * 60 * 60 * 1000L;
    CredentialsWithAuthoritiesDto credentialsWithAuthoritiesDto =
        CredentialsWithAuthoritiesDto.builder()
            .username(username)
            .email(email)
            .authorities(authorities)
            .build();
    when(jwtProperties.getSecret()).thenReturn(secret);
    when(jwtProperties.getAccessToken())
        .thenReturn(JwtProperties.AccessTokenProperties.builder().ttl(accessTokenTtl).build());
    when(jwtProperties.getRefreshToken())
        .thenReturn(JwtProperties.RefreshTokenProperties.builder().ttl(refreshTokenTtl).build());

    // Do
    var jwtTokensDto = jwtService.createTokens(credentialsWithAuthoritiesDto);

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

  private Key getSigningKey(String secret) {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
