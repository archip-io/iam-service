package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.AuthenticationDto;
import com.archipio.iamservice.dto.CredentialsDto;

public interface AuthenticationService {

  CredentialsDto authenticate(AuthenticationDto authenticationDto);
}
