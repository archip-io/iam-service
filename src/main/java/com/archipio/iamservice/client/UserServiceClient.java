package com.archipio.iamservice.client;

import com.archipio.iamservice.dto.CredentialsDto;
import com.archipio.iamservice.dto.RegistrationDto;

public interface UserServiceClient {

  void saveCredentials(RegistrationDto registrationDto);

  CredentialsDto findCredentialsByLogin(String login);

  CredentialsDto findCredentialsByUsernameAndEmail(String username, String email);

  void resetPassword(String login, String password);

  void validatePassword(String login, String password);
}
