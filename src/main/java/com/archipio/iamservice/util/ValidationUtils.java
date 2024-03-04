package com.archipio.iamservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtils {

  public static final int MIN_USERNAME_LENGTH = 1;
  public static final int MAX_USERNAME_LENGTH = 30;
  public static final String USERNAME_REGEX = "^[a-zA-Z]\\w+$";
  public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
  public static final int MIN_PASSWORD_LENGTH = 8;
  public static final int MAX_PASSWORD_LENGTH = 64;
  public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[\\W_])(?=.*[0-9])(?=.*[a-z]).+$";
}
