package com.archipio.iamservice.dto;

import static com.archipio.iamservice.util.ValidationUtils.EMAIL_REGEX;
import static com.archipio.iamservice.util.ValidationUtils.MAX_PASSWORD_LENGTH;
import static com.archipio.iamservice.util.ValidationUtils.MAX_USERNAME_LENGTH;
import static com.archipio.iamservice.util.ValidationUtils.MIN_PASSWORD_LENGTH;
import static com.archipio.iamservice.util.ValidationUtils.MIN_USERNAME_LENGTH;
import static com.archipio.iamservice.util.ValidationUtils.PASSWORD_REGEX;
import static com.archipio.iamservice.util.ValidationUtils.USERNAME_REGEX;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredentialsDto {

  @NotNull(message = "{validation.username.not-null}")
  @Length(
      min = MIN_USERNAME_LENGTH,
      max = MAX_USERNAME_LENGTH,
      message = "{validation.username.length}")
  @Pattern(regexp = USERNAME_REGEX, message = "{validation.username.pattern}")
  private String username;

  @NotNull(message = "{validation.email.not-null}")
  @Email(regexp = EMAIL_REGEX, message = "{validation.email.email}")
  private String email;

  @NotNull(message = "{validation.password.not-null}")
  @Length(
      min = MIN_PASSWORD_LENGTH,
      max = MAX_PASSWORD_LENGTH,
      message = "{validation.password.length}")
  @Pattern(regexp = PASSWORD_REGEX, message = "{validation.password.pattern}")
  private String password;
}
