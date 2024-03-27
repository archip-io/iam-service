package com.archipio.iamservice.unittest.client.iml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.archipio.iamservice.client.impl.UserServiceClientImpl;
import com.archipio.iamservice.config.RestClientProperties;
import com.archipio.iamservice.dto.CredentialsDto;
import com.archipio.iamservice.exception.RestClientNotFoundException;
import com.archipio.iamservice.exception.RestClientUrlNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceClientImplTest {

  @Mock private RestTemplate restTemplate;
  @Mock private RestClientProperties restClientProperties;
  @InjectMocks private UserServiceClientImpl userServiceClient;

  @Test
  @Order(1)
  void saveCredentials_whenRestClientNotInitialize_thenThrownRestClientNotFoundException() {
    // Prepare
    when(restClientProperties.getClients()).thenReturn(new HashMap<>());

    // Do
    assertThatExceptionOfType(RestClientNotFoundException.class)
        .isThrownBy(() -> userServiceClient.saveCredentials(null));
  }

  @Test
  @Order(2)
  void saveCredentials_whenRestClientUrlNotInitialize_thenThrownRestClientUrlNotFoundException() {
    // Prepare
    when(restClientProperties.getClients())
        .thenReturn(
            Map.of("user-service", RestClientProperties.ClientProperties.builder().build()));

    // Do
    assertThatExceptionOfType(RestClientUrlNotFoundException.class)
        .isThrownBy(() -> userServiceClient.saveCredentials(null));
  }

  @Test
  void findCredentialsByLogin_whenCredentialsExists_thenReturnCredentials() {
    // Prepare
    final var login = "login";
    final var credentialsDto = CredentialsDto.builder().build();
    when(restClientProperties.getClients())
        .thenReturn(
            Map.of(
                "user-service",
                RestClientProperties.ClientProperties.builder()
                    .url("http://user-service")
                    .build()));
    when(restTemplate.exchange(anyString(), eq(GET), eq(null), eq(CredentialsDto.class)))
        .thenReturn(ResponseEntity.ok(credentialsDto));

    // Do
    var actualCredentialsDto = userServiceClient.findCredentialsByLogin(login);

    // Check
    verify(restTemplate, only()).exchange(anyString(), eq(GET), eq(null), eq(CredentialsDto.class));
    assertThat(actualCredentialsDto).isEqualTo(credentialsDto);
  }

  @Test
  void findCredentialsByLogin_whenCredentialsNotFound_thenReturnNull() {
    // Prepare
    final var login = "login";
    when(restClientProperties.getClients())
        .thenReturn(
            Map.of(
                "user-service",
                RestClientProperties.ClientProperties.builder()
                    .url("http://user-service")
                    .build()));
    when(restTemplate.exchange(anyString(), eq(GET), eq(null), eq(CredentialsDto.class)))
        .thenThrow(new HttpClientErrorException(NOT_FOUND));

    // Do
    var actualCredentialsDto = userServiceClient.findCredentialsByLogin(login);

    // Check
    verify(restTemplate, only()).exchange(anyString(), eq(GET), eq(null), eq(CredentialsDto.class));
    assertThat(actualCredentialsDto).isNull();
  }

  @Test
  void findCredentialsByLogin_whenUnexpectedError_thenThrownHttpClientErrorException() {
    // Prepare
    final var login = "login";
    when(restClientProperties.getClients())
        .thenReturn(
            Map.of(
                "user-service",
                RestClientProperties.ClientProperties.builder()
                    .url("http://user-service")
                    .build()));
    when(restTemplate.exchange(anyString(), eq(GET), eq(null), eq(CredentialsDto.class)))
        .thenThrow(new HttpClientErrorException(BAD_REQUEST));

    // Do
    assertThatExceptionOfType(HttpClientErrorException.class)
        .isThrownBy(() -> userServiceClient.findCredentialsByLogin(login));
  }

  @Test
  void findCredentialsByUsernameAndEmail_whenCredentialsExists_thenReturnCredentials() {
    // Prepare
    final var username = "username";
    final var email = "email";
    final var credentialsDto = CredentialsDto.builder().build();
    when(restClientProperties.getClients())
        .thenReturn(
            Map.of(
                "user-service",
                RestClientProperties.ClientProperties.builder()
                    .url("http://user-service")
                    .build()));
    when(restTemplate.exchange(anyString(), eq(GET), eq(null), eq(CredentialsDto.class)))
        .thenReturn(ResponseEntity.ok(credentialsDto));

    // Do
    var actualCredentialsDto = userServiceClient.findCredentialsByUsernameAndEmail(username, email);

    // Check
    verify(restTemplate, only()).exchange(anyString(), eq(GET), eq(null), eq(CredentialsDto.class));
    assertThat(actualCredentialsDto).isEqualTo(credentialsDto);
  }

  @Test
  void findCredentialsByUsernameAndEmail_whenCredentialsNotFound_thenReturnNull() {
    // Prepare
    final var username = "username";
    final var email = "email";
    when(restClientProperties.getClients())
        .thenReturn(
            Map.of(
                "user-service",
                RestClientProperties.ClientProperties.builder()
                    .url("http://user-service")
                    .build()));
    when(restTemplate.exchange(anyString(), eq(GET), eq(null), eq(CredentialsDto.class)))
        .thenThrow(new HttpClientErrorException(NOT_FOUND));

    // Do
    var actualCredentialsDto = userServiceClient.findCredentialsByUsernameAndEmail(username, email);

    // Check
    verify(restTemplate, only()).exchange(anyString(), eq(GET), eq(null), eq(CredentialsDto.class));
    assertThat(actualCredentialsDto).isNull();
  }

  @Test
  void findCredentialsByUsernameAndEmail_whenUnexpectedError_thenThrownHttpClientErrorException() {
    // Prepare
    final var username = "username";
    final var email = "email";
    when(restClientProperties.getClients())
        .thenReturn(
            Map.of(
                "user-service",
                RestClientProperties.ClientProperties.builder()
                    .url("http://user-service")
                    .build()));
    when(restTemplate.exchange(anyString(), eq(GET), eq(null), eq(CredentialsDto.class)))
        .thenThrow(new HttpClientErrorException(BAD_REQUEST));

    // Do
    assertThatExceptionOfType(HttpClientErrorException.class)
        .isThrownBy(() -> userServiceClient.findCredentialsByUsernameAndEmail(username, email));
  }
}
