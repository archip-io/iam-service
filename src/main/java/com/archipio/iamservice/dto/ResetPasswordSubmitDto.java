package com.archipio.iamservice.dto;

import static com.archipio.iamservice.util.ValidationUtils.MAX_PASSWORD_LENGTH;
import static com.archipio.iamservice.util.ValidationUtils.MIN_PASSWORD_LENGTH;
import static com.archipio.iamservice.util.ValidationUtils.PASSWORD_REGEX;

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
public class ResetPasswordSubmitDto {

  @NotNull(message = "{validation.password.not-null}")
  @Length(
      min = MIN_PASSWORD_LENGTH,
      max = MAX_PASSWORD_LENGTH,
      message = "{validation.password.length}")
  @Pattern(regexp = PASSWORD_REGEX, message = "{validation.password.pattern}")
  private String password;

  @NotNull(message = "{validation.token.not-null}")
  private String token;
}
