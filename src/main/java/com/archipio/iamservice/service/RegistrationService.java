package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.RegistrationDto;

public interface RegistrationService {

  void register(RegistrationDto registrationDto);

  void confirmRegistration(String token);
}
