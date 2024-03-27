package com.archipio.iamservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokensDto {

  @JsonProperty("access_token")
  @Schema(description = "Токен доступа", requiredMode = Schema.RequiredMode.REQUIRED)
  private String accessToken;

  @JsonProperty("refresh_token")
  @Schema(description = "Токен обновления", requiredMode = Schema.RequiredMode.REQUIRED)
  private String refreshToken;
}
