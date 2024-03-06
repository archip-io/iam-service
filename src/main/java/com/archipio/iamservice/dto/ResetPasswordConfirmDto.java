package com.archipio.iamservice.dto;

import static com.archipio.iamservice.util.ValidationUtils.MAX_PASSWORD_LENGTH;
import static com.archipio.iamservice.util.ValidationUtils.MIN_PASSWORD_LENGTH;
import static com.archipio.iamservice.util.ValidationUtils.PASSWORD_REGEX;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class ResetPasswordConfirmDto {

  @NotNull(message = "{validation.password.not-null}")
  @Length(
      min = MIN_PASSWORD_LENGTH,
      max = MAX_PASSWORD_LENGTH,
      message = "{validation.password.length}")
  @Pattern(regexp = PASSWORD_REGEX, message = "{validation.password.pattern}")
  @Schema(description = "Пароль", requiredMode = Schema.RequiredMode.REQUIRED)
  private String password;
}
