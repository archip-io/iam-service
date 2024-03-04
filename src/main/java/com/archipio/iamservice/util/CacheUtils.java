package com.archipio.iamservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheUtils {

  public static final long CREDENTIALS_TTL_S = 5 * 60L;
}
