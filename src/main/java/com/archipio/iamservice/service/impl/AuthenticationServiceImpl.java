package com.archipio.iamservice.service.impl;

import com.archipio.iamservice.client.UserServiceClient;
import com.archipio.iamservice.dto.AuthenticationDto;
import com.archipio.iamservice.dto.JwtTokensDto;
import com.archipio.iamservice.exception.CredentialsNotFoundException;
import com.archipio.iamservice.exception.InvalidOrExpiredConfirmationTokenException;
import com.archipio.iamservice.service.AuthenticationService;
import com.archipio.iamservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final JwtService jwtService;
  private final UserServiceClient userServiceClient;

  @Override
  public JwtTokensDto authenticate(AuthenticationDto authenticationDto) {
    userServiceClient.validatePassword(
        authenticationDto.getLogin(), authenticationDto.getPassword());

    var credentialsDto = userServiceClient.findCredentialsByLogin(authenticationDto.getLogin());
    if (credentialsDto == null) {
      throw new CredentialsNotFoundException();
    }

    return jwtService.createTokens(credentialsDto);
  }

  @Override
  public JwtTokensDto refresh(String token) {
    if (!jwtService.validate(token)) {
      throw new InvalidOrExpiredConfirmationTokenException();
    }
    var username = jwtService.extractUsername(token);
    var email = jwtService.extractEmail(token);

    var credentialsDto = userServiceClient.findCredentialsByUsernameAndEmail(username, email);
    if (credentialsDto == null) {
      throw new CredentialsNotFoundException();
    }

    return jwtService.createTokens(credentialsDto);
  }
}
