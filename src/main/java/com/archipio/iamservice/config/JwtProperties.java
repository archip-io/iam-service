package com.archipio.iamservice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@ConfigurationProperties(prefix = "jwt", ignoreUnknownFields = false)
public class JwtProperties {

  private String secret;
  private AccessTokenProperties accessToken;
  private RefreshTokenProperties refreshToken;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class AccessTokenProperties {

    private long ttl;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class RefreshTokenProperties {

    private long ttl;
  }
}
