package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.AuthenticationDto;
import com.archipio.iamservice.dto.JwtTokensDto;

public interface AuthenticationService {

  JwtTokensDto authenticate(AuthenticationDto authenticationDto);

  JwtTokensDto refresh(String token);
}
