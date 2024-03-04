package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.CredentialsInputDto;

public interface RegistrationService {

  void register(CredentialsInputDto inputDto);
}
