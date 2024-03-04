package com.archipio.iamservice.cache.entity;

import static com.archipio.iamservice.util.CacheUtils.CREDENTIALS_TTL_S;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "credentials", timeToLive = CREDENTIALS_TTL_S)
public class CredentialsCache {

  @Id private String token;

  private String username;
  private String email;
  private String password;
}
