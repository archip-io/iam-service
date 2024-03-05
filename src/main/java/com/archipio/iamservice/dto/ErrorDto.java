package com.archipio.iamservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
  private Instant createdAt;

  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, String> errors;
}
