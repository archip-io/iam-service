package com.archipio.iamservice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@ConfigurationProperties(prefix = "jwt", ignoreUnknownFields = false)
public class JwtProperties {

  private String secret;
  private AccessTokenProperties accessToken;
  private RefreshTokenProperties refreshToken;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class AccessTokenProperties {

    private long ttl;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class RefreshTokenProperties {

    private long ttl;
  }
}
