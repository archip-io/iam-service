package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.ConfirmationTokenDto;
import com.archipio.iamservice.dto.RegistrationDto;

public interface RegistrationService {

  void register(RegistrationDto registrationDto);

  void submitRegistration(ConfirmationTokenDto confirmationTokenDto);
}
