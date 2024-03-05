package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.CredentialsDto;
import com.archipio.iamservice.dto.TokenDto;

public interface RegistrationService {

  void register(CredentialsDto credentialsDto);

  void submitRegistration(TokenDto tokenDto);
}
