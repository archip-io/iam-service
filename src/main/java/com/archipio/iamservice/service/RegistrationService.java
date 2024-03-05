package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.RegistrationDto;
import com.archipio.iamservice.dto.RegistrationSubmitDto;

public interface RegistrationService {

  void register(RegistrationDto registrationDto);

  void submitRegistration(RegistrationSubmitDto registrationSubmitDto);
}
