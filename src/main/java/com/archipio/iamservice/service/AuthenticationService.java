package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.AuthenticationDto;
import com.archipio.iamservice.dto.CredentialsWithAuthoritiesDto;

public interface AuthenticationService {

  CredentialsWithAuthoritiesDto authenticate(AuthenticationDto authenticationDto);
}
