package com.archipio.iamservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationDto {

  @NotNull(message = "{validation.login.not-null}")
  private String login;

  @NotNull(message = "{validation.password.not-null}")
  private String password;
}
