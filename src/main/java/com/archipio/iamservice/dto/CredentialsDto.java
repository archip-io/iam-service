package com.archipio.iamservice.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredentialsDto {

  private String username;
  private String email;
  private Boolean isEnabled;
  private List<String> authorities;
}
