package com.archipio.iamservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheUtils {

  public static final long REGISTRATION_CACHE_TTL_S = 5 * 60L;
  public static final long RESET_PASSWORD_CACHE_TTL_S = 5 * 60L;
}
