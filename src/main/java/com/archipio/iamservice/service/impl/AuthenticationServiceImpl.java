package com.archipio.iamservice.service.impl;

import com.archipio.iamservice.dto.AuthenticationDto;
import com.archipio.iamservice.dto.CredentialsDto;
import com.archipio.iamservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  @Override
  public CredentialsDto authenticate(AuthenticationDto authenticationDto) {
    // TODO: Запросить учётные данные у User Service

    // TODO: Проверить пароль

    // TODO: Вернуть учётные данные с правами доступа

    return null;
  }
}
