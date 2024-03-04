package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.CredentialsInputDto;
import com.archipio.iamservice.dto.TokenInputDto;

public interface RegistrationService {

  void register(CredentialsInputDto inputDto);

  void submitRegistration(TokenInputDto inputDto);
}
