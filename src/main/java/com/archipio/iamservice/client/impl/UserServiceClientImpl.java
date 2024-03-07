package com.archipio.iamservice.client.impl;

import static java.lang.String.format;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.archipio.iamservice.client.UserServiceClient;
import com.archipio.iamservice.config.RestClientProperties;
import com.archipio.iamservice.dto.CredentialsDto;
import com.archipio.iamservice.dto.RegistrationDto;
import com.archipio.iamservice.exception.RestClientNotFoundException;
import com.archipio.iamservice.exception.RestClientUrlNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class UserServiceClientImpl implements UserServiceClient {

  private static final String USER_SERVICE_NAME = "user-service";
  private static final String SAVE_CREDENTIALS_PATH = "users/sys/v0";
  private static final String FIND_CREDENTIALS_PATH = "users/sys/v0";
  private static final String RESET_PASSWORD_PATH = "users/sys/v0/reset-password";
  private static final String VALIDATE_PASSWORD_PATH = "users/sys/v0/validate-password";

  private static String userServiceUrl;

  private final RestTemplate restTemplate;

  private final RestClientProperties restClientProperties;

  @Override
  public void saveCredentials(RegistrationDto registrationDto) {
    var uri =
        UriComponentsBuilder.fromHttpUrl(getUserServiceUrl())
            .path(SAVE_CREDENTIALS_PATH)
            .encode(StandardCharsets.UTF_8)
            .build()
            .toUriString();
    var body = new HttpEntity<>(registrationDto);
    restTemplate.exchange(uri, POST, body, Void.class);
  }

  @Override
  public CredentialsDto findCredentialsByLogin(String login) {
    var uri =
        UriComponentsBuilder.fromHttpUrl(getUserServiceUrl())
            .path(FIND_CREDENTIALS_PATH)
            .queryParam("login", login)
            .encode(StandardCharsets.UTF_8)
            .build()
            .toUriString();
    try {
      return restTemplate.exchange(uri, GET, null, CredentialsDto.class).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().isSameCodeAs(NOT_FOUND)) {
        return null;
      }
      throw e;
    }
  }

  @Override
  public CredentialsDto findCredentialsByUsernameAndEmail(String username, String email) {
    var uri =
        UriComponentsBuilder.fromHttpUrl(getUserServiceUrl())
            .path(FIND_CREDENTIALS_PATH)
            .queryParam("username", username)
            .queryParam("email", email)
            .encode(StandardCharsets.UTF_8)
            .build()
            .toUriString();
    try {
      return restTemplate.exchange(uri, GET, null, CredentialsDto.class).getBody();
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().isSameCodeAs(NOT_FOUND)) {
        return null;
      }
      throw e;
    }
  }

  @Override
  public void resetPassword(String login, String password) {
    var uri =
        UriComponentsBuilder.fromHttpUrl(getUserServiceUrl())
            .path(RESET_PASSWORD_PATH)
            .encode(StandardCharsets.UTF_8)
            .build()
            .toUriString();
    var body =
        new HttpEntity<>(
            Map.of(
                "login", login,
                "password", password));
    restTemplate.exchange(uri, PUT, body, Void.class);
  }

  @Override
  public void validatePassword(String login, String password) {
    var uri =
        UriComponentsBuilder.fromHttpUrl(getUserServiceUrl())
            .path(VALIDATE_PASSWORD_PATH)
            .encode(StandardCharsets.UTF_8)
            .build()
            .toUriString();
    var body =
        new HttpEntity<>(
            Map.of(
                "login", login,
                "password", password));
    restTemplate.exchange(uri, POST, body, Void.class);
  }

  private String getUserServiceUrl() {
    if (userServiceUrl == null) {
      var clientProperties = restClientProperties.getClients().get(USER_SERVICE_NAME);
      if (clientProperties == null) {
        throw new RestClientNotFoundException(format("Client %s is null", USER_SERVICE_NAME));
      }

      userServiceUrl = clientProperties.getUrl();
      if (userServiceUrl == null) {
        throw new RestClientUrlNotFoundException(
            format("Client %s URL is null", USER_SERVICE_NAME));
      }
    }
    return userServiceUrl;
  }
}
