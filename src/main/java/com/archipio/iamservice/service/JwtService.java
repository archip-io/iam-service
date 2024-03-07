package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.CredentialsDto;
import com.archipio.iamservice.dto.JwtTokensDto;

public interface JwtService {

  JwtTokensDto createTokens(CredentialsDto credentialsDto);

  String extractUsername(String token);

  String extractEmail(String token);

  boolean validate(String token);
}
