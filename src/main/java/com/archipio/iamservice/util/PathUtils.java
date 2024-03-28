package com.archipio.iamservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathUtils {

  public static final String API_V0_PREFIX = "/api/v0/iam";
  public static final String SYS_V0_PREFIX = "/sys/v0/iam";
  public static final String AUTHENTICATION_SUFFIX = "/authenticate";
  public static final String REFRESH_TOKENS_SUFFIX = "/refresh-tokens";
  public static final String REGISTRATION_SUFFIX = "/register";
  public static final String REGISTRATION_CONFIRM_SUFFIX = "/register/confirm";
  public static final String RESET_PASSWORD_SUFFIX = "/reset-password";
  public static final String RESET_PASSWORD_CONFIRM_SUFFIX = "/reset-password/confirm";
  public static final String VALIDATE_TOKEN_SUFFIX = "/validate-token";
}
