package com.archipio.iamservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiUtils {

  public static final String API_V0_PREFIX = "/api/v0/iam";
  public static final String REGISTRATION_SUFFIX = "/register";
  public static final String REGISTRATION_SUBMIT_SUFFIX = "/register/submit";
  public static final String AUTHENTICATION_SUFFIX = "/authenticate";
  public static final String RESET_PASSWORD_SUFFIX = "/reset-password";
  public static final String RESET_PASSWORD_SUBMIT_SUFFIX = "/reset-password/submit";
}
