package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.CredentialsWithAuthoritiesDto;
import com.archipio.iamservice.dto.JwtTokensDto;

public interface JwtService {

  JwtTokensDto createTokens(CredentialsWithAuthoritiesDto credentialsWithAuthoritiesDto);
}
