package com.archipio.iamservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationDto {

  @NotNull(message = "{validation.login.not-null}")
  private String login;

  @NotNull(message = "{validation.password.not-null}")
  private String password;
}
