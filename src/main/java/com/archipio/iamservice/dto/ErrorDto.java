package com.archipio.iamservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;
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
public class ErrorDto {

  @JsonProperty("created_at")
  @Schema(description = "Время создания", requiredMode = Schema.RequiredMode.REQUIRED)
  private Instant createdAt;

  @Schema(description = "Сообщение ошибки", requiredMode = Schema.RequiredMode.REQUIRED)
  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(
      description = "Название поля и его ошибки",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private Map<String, String> errors;
}
