package com.archipio.iamservice.service.impl;

import com.archipio.iamservice.dto.AuthenticationDto;
import com.archipio.iamservice.dto.JwtTokensDto;
import com.archipio.iamservice.exception.InvalidOrExpiredConfirmationTokenException;
import com.archipio.iamservice.service.AuthenticationService;
import com.archipio.iamservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final JwtService jwtService;

  @Override
  public JwtTokensDto authenticate(AuthenticationDto authenticationDto) {
    // TODO: Запросить учётные данные у User Service

    // TODO: Проверить пароль

    // return jwtService.createTokens(credentialsDto);

    return null;
  }

  @Override
  public JwtTokensDto refresh(String token) {
    if (!jwtService.validate(token)) {
      throw new InvalidOrExpiredConfirmationTokenException();
    }
    var username = jwtService.extractUsername(token);

    // TODO: Запросить учётные данные по имени пользователя у User Service

    // return jwtService.createTokens(credentialsDto);

    return null;
  }
}
